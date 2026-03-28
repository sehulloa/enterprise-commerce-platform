# Enterprise Commerce Platform — Current Project Status

## Implemented Modules

- common
- identity-access
- catalog
- inventory
- orders
- payments
- app

---

## Implemented Features

### Security
- JWT authentication
- Spring Security configuration

### Catalog
- Product management
- Pricing

### Inventory
- Stock management
- Reservations

### Orders
- Order creation
- Order retrieval
- Integration with catalog (pricing)
- Integration with inventory

### Payments
- Payment creation
- Payment confirmation
- Business validations
- Event publication
- Hardening

## Modules In Progress / Pending

- customers
- branches
- notifications
---

## Testing

- Unit tests (service layer)
- Integration tests (end-to-end flow)
- Testcontainers implemented but currently disabled

---

## Technical Decisions

- Modular monolith architecture
- Java 21 + Spring Boot 3
- Gradle multi-module
- Layered architecture (controller → service → repository)
- Validations in service layer
- Mappers without business logic
- Mappers located in services (temporary)
- Enums inside `domain.enumtype`
- MapStruct configured but not used yet

---

## Known Issues

- Testcontainers not working on local Windows environment
- Docker client strategy issue (npipe / docker_cli)
- Docker smoke test failing (not related to business logic)

---

## Current Phase

**Phase 10 — Technical Reorganization**

Focus:
- Align documentation with real implementation
- Standardize module structure
- Clean dependencies
- Define project conventions