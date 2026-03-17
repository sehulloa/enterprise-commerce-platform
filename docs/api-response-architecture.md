# API Response Architecture

## 1. Objetivo

El sistema utiliza un **formato estándar de respuestas API** para garantizar que **todas las respuestas HTTP tengan la misma estructura**.

Esto permite:

* consistencia en toda la API
* manejo de errores uniforme
* facilitar integración con frontend
* facilitar debugging
* agregar metadata en el futuro

Además, el sistema utiliza **status HTTP correctos** mediante `ResponseEntity`.

Esto separa claramente:

| Responsabilidad        | Componente                     |
| ---------------------- | ------------------------------ |
| estructura del body    | ApiResponse / ApiErrorResponse |
| creación de respuestas | ApiResponseFactory             |
| status HTTP            | ResponseEntity                 |
| manejo de errores      | GlobalExceptionHandler         |

---

# 2. Estructura estándar de respuesta

Las respuestas exitosas utilizan el modelo:

```text
ApiResponse<T>
```

Ejemplo:

```json
{
  "success": true,
  "message": "Product retrieved successfully",
  "data": {
    "id": 10,
    "name": "Laptop"
  },
  "timestamp": "2026-03-16T00:10:22",
  "traceId": "..."
}
```

Campos:

| Campo     | Descripción                              |
| --------- | ---------------------------------------- |
| success   | indica si la operación fue exitosa       |
| message   | mensaje descriptivo                      |
| data      | objeto de respuesta                      |
| timestamp | momento en que ocurrió la respuesta      |
| traceId   | identificador para rastrear la operación |

---

# 3. Clase ApiResponse

`ApiResponse` es el **wrapper genérico de todas las respuestas exitosas**.

Ejemplo conceptual:

```java
public class ApiResponse<T> {

    private boolean success;

    private String message;

    private T data;

    private String timestamp;

    private String traceId;
}
```

El `<T>` significa que puede contener cualquier tipo de dato.

Ejemplos:

```java
ApiResponse<ProductResponse>
ApiResponse<List<ProductResponse>>
ApiResponse<Integer>
ApiResponse<Void>
```

---

# 4. ApiErrorResponse

Los errores utilizan un modelo separado:

```text
ApiErrorResponse
```

Ejemplo conceptual:

```java
public class ApiErrorResponse {

    private boolean success;

    private String message;

    private String error;

    private String timestamp;

    private String traceId;
}
```

Esto permite separar claramente:

* respuestas exitosas
* respuestas de error

---

# 5. ApiResponseFactory

En lugar de crear respuestas manualmente, se usa una **factory**.

Esto evita repetir código en todos los controllers.

Ejemplo:

```java
ApiResponseFactory.success(data, "Product created successfully");
```

La factory se encarga de:

* generar `timestamp`
* generar `traceId`
* construir el objeto de respuesta

Ejemplo de uso:

```java
return ApiResponseFactory.success(product, "Product created successfully");
```

---

# 6. Uso de ResponseEntity

Los controllers utilizan:

```java
ResponseEntity<ApiResponse<T>>
```

Esto permite controlar correctamente el **status HTTP**.

Ejemplo:

```java
@PostMapping
public ResponseEntity<ApiResponse<Product>> createProduct(
        @RequestBody CreateProductRequest request) {

    Product product = productService.createProduct(request);

    return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ApiResponseFactory.success(product, "Product created successfully"));
}
```

---

# 7. Convención de status HTTP

| Operación          | Status                    |
| ------------------ | ------------------------- |
| GET                | 200 OK                    |
| POST create        | 201 CREATED               |
| PUT / PATCH        | 200 OK                    |
| DELETE             | 204 NO CONTENT o 200      |
| Validation error   | 400 BAD REQUEST           |
| Resource not found | 404 NOT FOUND             |
| Business conflict  | 409 CONFLICT              |
| Unexpected error   | 500 INTERNAL SERVER ERROR |

---

# 8. Manejo centralizado de errores

Los errores **no se generan en los controllers**.

Se manejan en un:

```text
GlobalExceptionHandler
```

que captura excepciones y devuelve una respuesta uniforme.

Ejemplo conceptual:

```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiErrorResponse> handleRuntimeException(RuntimeException ex) {

        ApiErrorResponse response = ApiResponseFactory.error(
                "Unexpected internal error",
                ex.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }
}
```

Esto evita tener `try/catch` en cada controller.

---

# 9. Ejemplo de error

Si un servicio lanza:

```java
throw new RuntimeException("Product not found");
```

el `GlobalExceptionHandler` transforma la respuesta en:

```json
{
  "success": false,
  "message": "Unexpected internal error",
  "error": "Product not found",
  "timestamp": "2026-03-16T00:11:05",
  "traceId": "..."
}
```

con status HTTP:

```text
500 INTERNAL SERVER ERROR
```

---

# 10. Validaciones

Cuando una validación falla:

```java
@NotNull
private Long productId;
```

si el request viene sin ese campo:

```json
{
  "quantity": 10
}
```

Spring lanza una excepción de validación.

El `GlobalExceptionHandler` la transforma en:

```json
{
  "success": false,
  "message": "Validation error",
  "error": "productId must not be null",
  "timestamp": "...",
  "traceId": "..."
}
```

con status HTTP:

```text
400 BAD REQUEST
```

---

# 11. Beneficios del diseño

### Consistencia

Todas las respuestas tienen la misma estructura.

---

### Fácil integración frontend

El frontend siempre sabe que existen:

```text
success
message
data
```

---

### Status HTTP correctos

El uso de `ResponseEntity` permite respetar el estándar REST.

---

### Debugging más sencillo

`traceId` permite correlacionar logs.

---

### Controllers más limpios

Los controllers solo construyen respuestas mediante:

```java
ApiResponseFactory.success(...)
```

y delegan errores al `GlobalExceptionHandler`.

---

# 12. Flujo completo de respuesta

Flujo cuando llega un request:

```
Client
   ↓
Controller
   ↓
Service
   ↓
Repository
```

Respuesta exitosa:

```
Service
   ↓
Controller
   ↓
ApiResponseFactory
   ↓
ResponseEntity
   ↓
HTTP Response
```

Error:

```
Service
   ↓
Exception
   ↓
GlobalExceptionHandler
   ↓
ApiErrorResponse
   ↓
ResponseEntity
   ↓
HTTP Response
```

---

# 13. Ejemplo real del proyecto

Endpoint:

```http
GET /catalog/products/1
```

Respuesta:

```json
{
  "success": true,
  "message": "Product retrieved successfully",
  "data": {
    "id": 1,
    "sku": "LAPTOP-001",
    "name": "Laptop Dell",
    "categoryId": 1,
    "currentPrice": 899.99
  },
  "timestamp": "2026-03-16T00:20:00",
  "traceId": "..."
}
```

---

# 14. Conclusión

El sistema de respuestas implementado en la plataforma:

* estandariza todas las respuestas API
* utiliza **status HTTP correctos**
* centraliza el manejo de errores
* simplifica los controllers
* mejora la trazabilidad mediante `traceId`

Este enfoque es común en **APIs empresariales modernas**, especialmente en arquitecturas modulares o microservicios.


# 15. Error Codes y BusinessException

En sistemas empresariales es común utilizar **códigos de error estructurados** además de los mensajes de error.

Esto permite:

* identificar errores de forma consistente
* facilitar integración con frontend
* permitir internacionalización
* mejorar logging y observabilidad
* evitar depender únicamente de mensajes de texto

---

# 16. ErrorCode

Se define un enum que representa los errores conocidos del sistema.

Ejemplo conceptual:

```java
public enum ErrorCode {

    RESOURCE_NOT_FOUND,

    VALIDATION_ERROR,

    BUSINESS_RULE_VIOLATION,

    STOCK_NOT_AVAILABLE,

    DUPLICATE_RESOURCE,

    INTERNAL_ERROR
}
```

Cada código representa **una categoría de error de negocio o sistema**.

---

# 17. BusinessException

Las reglas de negocio pueden lanzar una excepción específica en lugar de usar `RuntimeException`.

Ejemplo conceptual:

```java
public class BusinessException extends RuntimeException {

    private final ErrorCode errorCode;

    public BusinessException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
```

Esto permite distinguir errores de negocio de errores técnicos.

---

# 18. Uso en servicios

Ejemplo en un servicio:

```java
if (availableStock < requestedQuantity) {
    throw new BusinessException(
        ErrorCode.STOCK_NOT_AVAILABLE,
        "Not enough stock available"
    );
}
```

Esto representa un **error de negocio esperado**, no un error técnico.

---

# 19. Manejo en GlobalExceptionHandler

El `GlobalExceptionHandler` puede capturar `BusinessException` y devolver un status HTTP adecuado.

Ejemplo conceptual:

```java
@ExceptionHandler(BusinessException.class)
public ResponseEntity<ApiErrorResponse> handleBusinessException(BusinessException ex) {

    ApiErrorResponse response = ApiResponseFactory.error(
        "Business rule violation",
        ex.getMessage()
    );

    return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(response);
}
```

En este caso se utiliza:

```text
409 CONFLICT
```

porque representa un conflicto con reglas de negocio.

---

# 20. Ejemplo de respuesta con ErrorCode

Ejemplo de respuesta final:

```json
{
  "success": false,
  "message": "Business rule violation",
  "error": "Not enough stock available",
  "errorCode": "STOCK_NOT_AVAILABLE",
  "timestamp": "2026-03-16T01:10:22",
  "traceId": "..."
}
```

Esto permite que el cliente identifique el error mediante el campo:

```text
errorCode
```

en lugar de depender del texto del mensaje.

---

# 21. Beneficios del uso de Error Codes

El uso de códigos de error estructurados ofrece varias ventajas:

### Integración con frontend

El frontend puede reaccionar a errores específicos.

Ejemplo:

```text
STOCK_NOT_AVAILABLE → mostrar mensaje de inventario
VALIDATION_ERROR → resaltar campos inválidos
```

---

### Internacionalización

Los mensajes pueden traducirse sin cambiar el código de error.

---

### Observabilidad

Los logs pueden agrupar errores por tipo.

---

### Consistencia entre servicios

Si la plataforma evoluciona a microservicios, los mismos códigos pueden utilizarse en todos los servicios.

---

# 22. Evolución futura

En fases posteriores el sistema puede evolucionar para incluir:

* catálogo completo de `ErrorCodes`
* documentación pública de errores
* correlación distribuida de `traceId`
* integración con observabilidad y tracing

---

# Conclusión

El uso de:

```text
ApiResponse
ApiErrorResponse
ResponseEntity
GlobalExceptionHandler
BusinessException
ErrorCode
```

establece una **arquitectura robusta y escalable para manejo de respuestas y errores** en la plataforma.

Este enfoque es ampliamente utilizado en **sistemas empresariales y arquitecturas de microservicios**.
