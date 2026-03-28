# Enterprise Commerce Platform

## Overview
Enterprise Commerce Platform es una aplicación backend desarrollada como un **monolito modular**, diseñada para evolucionar hacia una arquitectura de microservicios.

El objetivo es construir una base sólida, escalable y mantenible para la gestión de operaciones comerciales (clientes, catálogo, inventario, órdenes y pagos).

---

## Architecture

- Modular Monolith
- Java 21
- Spring Boot 3
- Gradle multi-module
- PostgreSQL
- RabbitMQ (preparado para eventos)

Cada módulo está desacoplado a nivel de código y organizado por capas internas.

---

## Modules

- **common** → utilidades compartidas y configuraciones base
- **identity-access** → autenticación, autorización y JWT
- **customers** → gestión de clientes
- **branches** → sucursales
- **catalog** → productos y precios
- **inventory** → control de stock
- **orders** → gestión de órdenes
- **payments** → procesamiento de pagos
- **notifications** → notificaciones (preparado)
- **app** → punto de entrada de la aplicación

---

## Current Status

El proyecto ya cuenta con:

- Estructura base completa
- Seguridad JWT implementada
- Módulos funcionales:
    - customers
    - catalog
    - inventory
    - orders
    - payments
- Integración entre módulos mediante servicios
- Tests unitarios e integración
- Hardening en módulo de pagos

⚠️ Testcontainers está implementado pero no funcional en entorno Windows local.

---

## Tech Stack

- Java 21
- Spring Boot 3
- Spring Security
- Spring Data JPA
- PostgreSQL
- RabbitMQ
- Gradle (multi-module)
- Lombok
- MapStruct (configurado, no utilizado aún)
- Testcontainers (pendiente estabilización)

---

## Project Structure

Cada módulo sigue una arquitectura por capas:
<module> <br />
├── api <br />
│ ├── controller <br />
│ └── dto <br />
├── application <br />
│ ├── service <br />
│ └── event <br />
├── domain <br />
│ ├── model <br />
│ └── enumtype <br />
└── infrastructure <br />
&nbsp;&nbsp;&nbsp;├── repository <br />
&nbsp;&nbsp;&nbsp;└── messaging


Principios:

- Controller → Service → Repository
- Validaciones en capa de servicio
- Mappers sin lógica de negocio
- Enums ubicados en `domain.enumtype`

---

## How to Run

1. Clonar el repositorio
2. Configurar base de datos PostgreSQL
3. Configurar variables de entorno necesarias
4. Ejecutar:

```bash
gradlew bootRun
```

O ejecutar desde el módulo app.

Testing

Ejecutar:
```bash
gradlew clean test
```

Notas:

- Tests unitarios y de integración disponibles
- Testcontainers configurado pero no funcional en entorno Windows (problema de Docker client)

## Documentation
- Estado actual del proyecto:
  - docs/current-project-status.md
- Roadmap del proyecto:
  - docs/project-roadmap.md