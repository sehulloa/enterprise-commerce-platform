# Enterprise Commerce Platform — Project Roadmap

## Phase 1 — Inicialización del proyecto ✔
- Creación del repositorio
- Configuración inicial

## Phase 2 — Configuración base ✔
- Gradle multi-module
- Configuración base de Spring Boot

## Phase 3 — Common Module ✔
- Clases compartidas
- Configuración común

## Phase 4 — Identity Access ✔
- Seguridad
- JWT
- Configuración de autenticación/autorización

## Phase 5 — Orders Module ✔
- Creación de órdenes
- Consulta de órdenes

## Phase 6 — Catalog Module ✔
- Productos
- Precios

## Phase 7 — Inventory Module ✔
- Stock
- Reservas

## Phase 8 — Payments Module ✔
- Creación de pagos
- Confirmación de pagos
- Validaciones
- Publicación de eventos

## Phase 9 — Testing ✔
- Unit tests
- Integration tests
- Hardening

---

## Phase 10 — Technical Reorganization (CURRENT)

Objetivo:
- Alinear documentación con implementación real
- Limpieza de dependencias
- Estandarización de módulos
- Definición de convenciones técnicas

---

### Phase 10 — Technical Reorganization ✔

### Phase 11 — Customers ✔

### Phase 12 — Branches ✔

### Phase 13 — Events (RabbitMQ) ✔

### Phase 14 — Notifications ✔

---

### Phase 15 — API Documentation (Swagger / OpenAPI) ✔

- Documented all API endpoints
- Swagger UI enabled and accessible
- Consistent documentation across modules:
    - orders
    - payments
    - customers
    - branches
    - catalog
    - inventory
    - authentication
- Controllers kept clean (minimal annotations)

---

### Phase 16 — API Response Standardization & Error Handling ✔

- Documented ApiResponse and ApiErrorResponse in OpenAPI
- Linked standard success and error wrappers in controllers
- Hardened global exception handling
- Improved request body validation error formatting
- Added request parameter/path validation handling
- Kept Swagger documentation clean without overloading controllers

## Future Phases

### Phase 17 — Observability (Logging & Traceability)

### Phase 18 — Security Hardening

### Phase 19 — Testing Stabilization

### Phase 20 — API Hardening

### Phase 21 — Observability (Metrics & Monitoring)

### Phase 22 — CI/CD Hardening

### Phase 23 — Microservices Readiness