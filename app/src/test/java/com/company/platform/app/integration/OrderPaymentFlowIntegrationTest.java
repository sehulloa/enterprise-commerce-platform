package com.company.platform.app.integration;

import com.company.platform.orders.api.dto.CreateOrderItemRequest;
import com.company.platform.orders.api.dto.CreateOrderRequest;
import com.company.platform.payments.api.dto.CreatePaymentRequest;
import com.company.platform.payments.domain.enumtype.PaymentMethod;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Disabled("Testcontainers not working in local Windows environment")
class OrderPaymentFlowIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16")
            .withDatabaseName("ecp_test")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.datasource.driver-class-name", postgres::getDriverClassName);
    }

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String baseUrl() {
        return "http://localhost:" + port;
    }

    // 1. MÉTODO PARA LOGIN
    private String authenticateAndGetToken() {
        Map<String, String> loginRequest = Map.of(
                "username", "admin",
                "password", "Admin123*"
        );

        ResponseEntity<Map> response = restTemplate.postForEntity(
                baseUrl() + "/auth/login",
                loginRequest,
                Map.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();

        System.out.println("LOGIN RESPONSE: " + response.getBody());

        Map body = response.getBody();
        Map data = (Map) body.get("data");

        assertThat(data).isNotNull();
        assertThat(data.get("token")).isNotNull();

        return data.get("token").toString();
    }

    // 2. HELPER PARA HEADERS
    private HttpEntity<Object> authorizedEntity(Object body, String token){

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
        return new HttpEntity<>(body, headers);
    }

    @Test
    void should_complete_full_flow_successfully() {

        // 1. Crear orden
        String token = authenticateAndGetToken();

        CreateOrderItemRequest item = new CreateOrderItemRequest();
        item.setProductId(1L);
        item.setQuantity(1);

        CreateOrderRequest orderRequest = new CreateOrderRequest();
        orderRequest.setBranchId(1L);
        orderRequest.setCustomerId(1L);
        orderRequest.setItems(List.of(item));

        ResponseEntity<Map> orderResponse = restTemplate.exchange(
                baseUrl() + "/orders",
                HttpMethod.POST,
                authorizedEntity(orderRequest, token),
                Map.class
        );

        assertThat(orderResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        Map orderData = (Map) orderResponse.getBody().get("data");
        Long orderId = Long.valueOf(orderData.get("id").toString());
        String status = orderData.get("status").toString();

        assertThat(status).isEqualTo("RESERVED");

        BigDecimal totalAmount = new BigDecimal(orderData.get("totalAmount").toString());

        // 2. Crear payment
        CreatePaymentRequest paymentRequest = new CreatePaymentRequest();
        paymentRequest.setOrderId(orderId);
        paymentRequest.setAmount(totalAmount);
        paymentRequest.setMethod(PaymentMethod.CARD);

        ResponseEntity<Map> paymentResponse = restTemplate.exchange(
                baseUrl() + "/payments",
                HttpMethod.POST,
                authorizedEntity(paymentRequest, token),
                Map.class
        );

        assertThat(paymentResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        Map paymentData = (Map) paymentResponse.getBody().get("data");
        Long paymentId = Long.valueOf(paymentData.get("id").toString());

        // 3. Confirmar payment
        Map<String, String> confirmBody = Map.of("reference", "TEST-123");

        ResponseEntity<Map> confirmResponse = restTemplate.exchange(
                baseUrl() + "/payments/" + paymentId + "/confirm",
                HttpMethod.POST,
                authorizedEntity(confirmBody, token),
                Map.class
        );

        System.out.println("CONFIRM STATUS: " + confirmResponse.getStatusCode());
        System.out.println("CONFIRM BODY: " + confirmResponse.getBody());

        assertThat(confirmResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        Map confirmData = (Map) confirmResponse.getBody().get("data");

        assertThat(confirmData).containsEntry("status", "CAPTURED");

        // 4. Validar orden confirmada
        ResponseEntity<Map> getOrderResponse = restTemplate.exchange(
                baseUrl() + "/orders/" + orderId,
                HttpMethod.GET,
                authorizedEntity(null, token),
                Map.class
        );

        Map finalOrderData = (Map) getOrderResponse.getBody().get("data");

        assertThat(finalOrderData).containsEntry("status", "CONFIRMED");
    }

}

