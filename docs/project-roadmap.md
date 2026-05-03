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

---

### Phase 17 — Observability (Logging & Traceability) ✔

- Implemented Correlation ID filter (X-Correlation-Id)
- Added MDC traceId propagation across requests
- Included traceId in API responses
- Implemented request-level logging (method, path, status, duration)
- Added business-level logging in service layer
- Configured Logback (console + file logging)
- Enabled and configured Spring Boot Actuator

### Phase 18 — Security Hardening ✔

- Implemented JWT-based stateless authentication
- Externalized JWT configuration (secret and expiration)
- Added JwtAuthenticationFilter for request validation
- Implemented AuthenticationService and login flow
- Integrated CustomUserDetailsService with identity-access module
- Enabled method-level authorization with @PreAuthorize
- Applied permission-based access control (hasAuthority)
- Implemented proper error handling:
  - 401 Unauthorized (authentication issues)
  - 403 Forbidden (authorization issues)
- Fixed incorrect 500 responses in security flows
- Restricted Actuator endpoints (health, info only)
- Controlled Swagger exposure via configuration
- Standardized security responses using ApiResponse structure

### Phase 19 — Containerization & Runtime Environments ✔

- Added runtime profile separation (dev, docker, prod)
- Externalized environment-based configuration
- Added multi-stage Dockerfile for app runtime
- Added .dockerignore
- Expanded Docker Compose to include app, PostgreSQL and RabbitMQ
- Added .env.example for local runtime setup
- Hardened runtime configuration
- Added health checks for infrastructure and app
- Documented runtime and deployment workflow

### Phase 20 — CI/CD & Release Automation

- Added base GitHub Actions CI workflow
- Added SonarQube integration workflow
- Added Docker image publish workflow to GHCR
- Defined release branch strategy and versioning flow
- Preparing branch protection and deployment-oriented automation

## Future Phases

### Phase 21 — Deployment Automation

- Added production runtime profile and externalized configuration
- Hardened Docker image for production-oriented container runtime
- Added AWS ECS base deployment descriptors
- Added GitHub Actions workflow for ECR publish and ECS deployment flow
- Defined post-deployment validation checklist
- Prepared the project for AWS runtime execution

### Phase 22 — Infrastructure Provisioning & First AWS Runtime ✔

- Provisioned AWS ECR repository and pushed Docker image
- Created ECS cluster and executed container using Fargate
- Configured and connected AWS RDS PostgreSQL instance
- Configured and connected Amazon MQ (RabbitMQ) broker
- Validated application runtime in AWS environment
- Verified full backend functionality with real infrastructure
- Cleaned up AWS resources to avoid unnecessary costs


### Phase 23 — Backend Closure & Operational Readiness (In Progress)

- Implemented monitoring with Prometheus and Grafana
- Exposed application metrics via Spring Boot Actuator and Micrometer
- Validated metrics collection and visualization
- Preparing final backend documentation
- Preparing operational checklist and backend closure
