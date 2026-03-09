## Project Roadmap & Development Phases

Este documento describe todas las fases del desarrollo del sistema **Enterprise Commerce Platform**, incluyendo las sub-fases implementadas durante la construcción del proyecto.

Su objetivo es permitir continuar el desarrollo incluso en nuevas sesiones o con nuevos desarrolladores.

---

# 1. Descripción del proyecto

Enterprise Commerce Platform es un backend empresarial desarrollado con **Java 21 y Spring Boot**, siguiendo una arquitectura **Monolito Modular**, diseñada para evolucionar hacia **microservicios**.

El sistema simula un entorno real de desarrollo empresarial.

---

# 2. Stack tecnológico

Backend

* Java 21
* Spring Boot 3
* Spring Security
* Spring Data JPA

Infraestructura

* PostgreSQL
* RabbitMQ (planificado)
* Flyway (migraciones)

Build

* Gradle multi-module

Herramientas adicionales

* Lombok
* MapStruct
* JWT
* Actuator
* Logback
* Swagger/OpenAPI (planificado)
* Jenkins CI/CD (planificado)
* SonarQube (planificado)

---

# 3. Arquitectura del sistema

Arquitectura principal:

**Modular Monolith**

Cada módulo sigue esta estructura:

```text
api
application
domain
infrastructure
```

Esto permite migrar módulos a microservicios en el futuro.

---

# 4. Estructura del proyecto

```text
enterprise-commerce-platform
│
├── common
├── identity-access
├── customers
├── branches
├── catalog
├── inventory
├── orders
├── payments
├── notifications
└── app
```

---

# 5. Fases del desarrollo

## Fase 1 — Estructura del proyecto

Objetivo: crear la base del sistema.

Implementado:

* Gradle multi-module
* estructura de módulos
* configuración inicial Spring Boot
* estructura de paquetes por módulo
* integración inicial con Git

Resultado:

estructura base empresarial del proyecto.

---

## Fase 2 — Observabilidad básica

Objetivo: agregar herramientas básicas de monitoreo.

Implementado:

* logging con Logback
* configuración de logs estructurados
* Spring Boot Actuator
* endpoint `/actuator/health`

---

### Fase 2.5 — Mejora de observabilidad

Implementado:

* endpoint de prueba de logging
* endpoint de error controlado
* trazabilidad básica con `traceId`

---

## Fase 3 — Identity Access Module

Objetivo: implementar el sistema de identidad y permisos.

Entidades implementadas:

```text
User
Role
Permission
UserRole
RolePermission
```

Características:

* relaciones many-to-many mediante tablas puente
* modelo de autorización basado en roles y permisos
* migraciones Flyway

---

## Fase 4 — Seguridad

Objetivo: implementar autenticación y autorización.

### Fase 4.1 — Diseño de seguridad

Se diseñó la arquitectura de seguridad:

componentes definidos:

```text
AuthenticationController
AuthenticationService
CustomUserDetailsService
JwtService
JwtAuthenticationFilter
SecurityConfig
```

Se definió el flujo JWT completo.

---

### Fase 4.2 — Servicios de Identity Access

Implementado:

* UserService
* RoleService
* PermissionService

Consultas optimizadas para obtener:

* roles de usuario
* permisos de usuario

---

### Fase 4.3 — Implementación JWT

Implementado:

* Spring Security
* autenticación stateless
* generación de JWT
* validación de JWT
* filtro `JwtAuthenticationFilter`

Endpoint de autenticación:

```text
POST /auth/login
```

JWT contiene:

```text
username
roles
permissions
expiration
```

---

# 6. Documentación creada

Carpeta `/docs`:

```text
architecture.md
development-workflow.md
security-architecture.md
security-filter-chain.md
security-components.md
project-roadmap.md
```

Cada documento describe un aspecto específico de la arquitectura.

---

# 7. Estado actual del proyecto

Actualmente el sistema tiene implementado:

* arquitectura monolito modular
* seguridad JWT completa
* módulo identity-access funcional
* autenticación mediante `/auth/login`
* roles y permisos cargados desde base de datos

---

# 8. Próximas fases del proyecto

## Fase 5 — Orders Module

Implementar:

* Order
* OrderItem
* estados de pedido
* lógica de negocio de pedidos

---

## Fase 6 — Inventory Integration

Integrar inventario con pedidos.

---

## Fase 7 — Event Driven Architecture

Integración con:

* RabbitMQ
* eventos de dominio

---

## Fase 8 — Payments Module

Implementar módulo de pagos.

---

## Fase 9 — API Documentation

Integración con:

* Swagger / OpenAPI

---

## Fase 10 — CI/CD

Implementar:

* Jenkins
* pipelines de build
* análisis con SonarQube

---

# 9. Objetivo final del proyecto

El objetivo es construir un backend empresarial que incluya:

* arquitectura modular
* seguridad robusta
* integración con mensajería
* documentación completa
* pipeline de CI/CD

Este proyecto servirá como referencia para desarrollo de sistemas empresariales reales.
