# Enterprise Commerce Platform — Current Project Status

## Implemented Modules
- common
- identity-access
- customers
- branches
- catalog
- inventory
- orders
- payments
- notifications
- app

## Implemented Features

### Security
- JWT authentication
- Spring Security configuration

### Payments
- createPayment
- confirmPayment
- validations
- event publication

### Orders
- order creation
- order retrieval
- integration with catalog (price)
- integration with inventory

### Testing
- integration tests (working)
- unit tests (services)
- Testcontainers (implemented but disabled)

## Technical Decisions
- Modular monolith architecture
- Java 21 + Spring Boot 3
- Layered architecture (controller → service → repository)
- Validations in service layer
- Mappers without business logic
- Mappers located in services (temporary decision)
- Enums inside `domain.enumtype`
- Single branch per phase/module
- MapStruct NOT used (for now)

## Known Issues
- Testcontainers not working on Windows (Docker client strategy issue)
- Docker smoke test fails (not business related)

## Current Phase
- FASE 10 — Technical Reorganization