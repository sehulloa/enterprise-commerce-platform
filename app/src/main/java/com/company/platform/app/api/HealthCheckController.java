package com.company.platform.app.api;

import com.company.platform.common.api.exception.NotFoundException;
import com.company.platform.common.api.response.ApiResponse;
import com.company.platform.common.api.response.ApiResponseFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
public class HealthCheckController {

    @GetMapping("/test-log")
    public ApiResponse<Map<String, String>> testLog() {
        log.info("Test log endpoint invoked");
        return ApiResponseFactory.success(Map.of("status", "ok"), "Request processed successfully");
    }

    @GetMapping("/test-error")
    public ApiResponse<Void> testError() {
        throw new NotFoundException("Demo resource was not found");
    }
}
