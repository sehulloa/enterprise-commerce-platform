
# Enterprise Commerce Platform

## System Architecture

Este documento describe la arquitectura del sistema **Enterprise Commerce Platform**, incluyendo principios de diseño, estructura modular y evolución planificada hacia microservicios.

---

# 1. Visión general

Enterprise Commerce Platform es un sistema empresarial diseñado para gestionar operaciones de comercio como:

* gestión de clientes
* catálogo de productos
* inventario
* pedidos
* pagos
* notificaciones

El sistema está diseñado inicialmente como un **Monolito Modular**, permitiendo evolucionar progresivamente hacia **microservicios**.

---

# 2. Arquitectura general

El sistema sigue una arquitectura **Monolito Modular basada en dominios**.

```text
                   ┌─────────────────────────────┐
                  │           Clients           │
                  │ (Web / Mobile / External)  │
                  └──────────────┬─────────────┘
                                 │
                                 ▼
                     ┌───────────────────────┐
                     │   Security Layer      │
                     │ (Spring Security JWT) │
                     │                       │
                     │ JwtAuthenticationFilter
                     │ AuthenticationManager
                     │ SecurityContext       │
                     └───────────┬───────────┘
                                 │
                                 ▼
                      ┌────────────────────┐
                      │   REST API Layer   │
                      │   (Controllers)    │
                      └─────────┬──────────┘
                                │
                                ▼
                      ┌────────────────────┐
                      │ Application Layer  │
                      │ (Services / Usecases)
                      └─────────┬──────────┘
                                │
                                ▼
                      ┌────────────────────┐
                      │   Domain Modules   │
                      └─────────┬──────────┘
                                │
                                ▼
                      ┌────────────────────┐
                      │ Persistence Layer  │
                      │ (Spring Data JPA)  │
                      └─────────┬──────────┘
                                │
                                ▼
                           PostgreSQL
```

---

# 3. Estructura del proyecto

El sistema está organizado como un **Gradle Multi-Module Project**.

```text
enterprise-commerce-platform
│
├─ common
├─ identity-access
├─ customers
├─ branches
├─ catalog
├─ inventory
├─ orders
├─ payments
├─ notifications
└─ app
```

---

# 4. Descripción de módulos

## common

Contiene componentes compartidos por todo el sistema.

Ejemplos:

* entidades base
* auditoría
* manejo de errores
* respuestas API
* utilidades comunes

---

## identity-access

Responsable de la **gestión de identidad y autorización**.

Incluye:

* usuarios
* roles
* permisos
* autenticación
* autorización

Tablas principales:

```text
users
roles
permissions
user_roles
role_permissions
```

---

## customers

Gestión de clientes.

Responsabilidades:

* registro de clientes
* información de contacto
* historial de compras

---

## branches

Gestión de sucursales.

Responsabilidades:

* sucursales de la empresa
* localización
* configuración operativa

---

## catalog

Gestión del catálogo de productos.

Responsabilidades:

* productos
* categorías
* precios base

---

## inventory

Control de inventario.

Responsabilidades:

* stock por sucursal
* ajustes de inventario
* movimientos de stock

---

## orders

Gestión de pedidos.

Responsabilidades:

* creación de pedidos
* estados de pedido
* cálculo de totales

---

## payments

Gestión de pagos.

Responsabilidades:

* registro de pagos
* integración con métodos de pago
* conciliación

---

## notifications

Gestión de notificaciones.

Responsabilidades:

* eventos del sistema
* envío de notificaciones
* integración con colas

---

## app

Módulo principal que inicia la aplicación.

Contiene:

* configuración Spring Boot
* seguridad
* configuración de infraestructura
* inicialización del sistema

---

# 5. Principios de arquitectura

El sistema sigue estos principios de diseño:

## Modularidad

Cada módulo representa un **bounded context** con responsabilidades claras.

---

## Bajo acoplamiento

Los módulos no acceden directamente a repositorios de otros módulos.

La comunicación debe realizarse mediante:

* servicios
* eventos
* APIs internas

---

## Alta cohesión

Cada módulo encapsula completamente su dominio.

---

## Independencia de dominio

La lógica de negocio reside en el **dominio**, no en controladores ni repositorios.

---

## Migraciones controladas

Los cambios de base de datos se gestionan mediante:

```text
Flyway
```

Esto permite versionar el esquema de base de datos.

---

# 6. Arquitectura por capas

Cada módulo sigue una estructura en capas:

```text
api
application
domain
infrastructure
```

---

## API Layer

Responsable de:

* controladores REST
* DTOs
* validación de entrada

---

## Application Layer

Contiene:

* servicios
* casos de uso
* orquestación de lógica de negocio

---

## Domain Layer

Contiene:

* entidades
* enums
* lógica de dominio

---

## Infrastructure Layer

Contiene:

* repositorios
* integraciones externas
* configuración técnica

---

# 7. Manejo de errores

El sistema utiliza un modelo uniforme de respuesta API.

Ejemplo de respuesta exitosa:

```json
{
  "success": true,
  "data": {},
  "timestamp": "2026-03-06T18:00:00",
  "traceId": "abc123"
}
```

Ejemplo de error:

```json
{
  "success": false,
  "message": "Resource not found",
  "error": "Customer not found",
  "timestamp": "2026-03-06T18:00:00",
  "traceId": "abc123"
}
```

Esto permite:

* debugging más sencillo
* trazabilidad entre servicios
* monitoreo consistente

---

# 8. Observabilidad

El sistema incluye:

* logging estructurado
* `traceId` por request
* Spring Boot Actuator

Esto facilita:

* diagnóstico de errores
* monitoreo del sistema
* análisis de logs

---

# 9. Comunicación entre módulos

Inicialmente los módulos interactúan mediante **llamadas directas de servicios**.

Ejemplo:

```text
Orders → Inventory
Orders → Payments
```

---

# 10. Event Driven Architecture

El sistema está preparado para comunicación basada en eventos usando:

```text
RabbitMQ
```

Ejemplo de eventos futuros:

```text
OrderCreatedEvent
PaymentCompletedEvent
InventoryAdjustedEvent
```

Esto permite desacoplar módulos y escalar el sistema.

---

# 11. Evolución hacia microservicios

La arquitectura modular permite separar módulos en microservicios en el futuro.

Posible evolución:

```text
identity-service
customer-service
catalog-service
inventory-service
order-service
payment-service
notification-service
```

Estos servicios podrían comunicarse mediante:

* REST
* eventos RabbitMQ
* API Gateway

---

# 12. Infraestructura

Tecnologías principales:

Backend:

```text
Java 21
Spring Boot 3
Spring Security
Spring Data JPA
```

Base de datos:

```text
PostgreSQL
```

Mensajería:

```text
RabbitMQ
```

Observabilidad:

```text
Spring Boot Actuator
Logging con Logback
```

---

# 13. Seguridad del sistema

El sistema utiliza **Spring Security con autenticación basada en JWT (JSON Web Tokens)**.

La autenticación es **stateless**, lo que significa que el servidor no mantiene sesiones.

Cada request autenticado incluye un token JWT en el header:

```
Authorization: Bearer <JWT>
```

---

## Endpoint de autenticación

Los usuarios se autentican mediante:

```
POST /auth/login
```

Request:

```json
{
  "username": "admin",
  "password": "password"
}
```

Respuesta:

```json
{
  "success": true,
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIs...",
    "type": "Bearer",
    "username": "admin"
  }
}
```

---

## Flujo de autenticación

El flujo de autenticación es el siguiente:

```
Client
  │
  │ POST /auth/login
  ▼
AuthenticationController
  │
  ▼
AuthenticationService
  │
  ▼
AuthenticationManager
  │
  ▼
CustomUserDetailsService
  │
  ▼
Database
  │
  ▼
JwtService
  │
  ▼
Generate JWT
  │
  ▼
Client receives token
```

---

## Flujo de autorización

Para endpoints protegidos:

```
Client Request
Authorization: Bearer <JWT>
        │
        ▼
JwtAuthenticationFilter
        │
        ▼
Validate Token
        │
        ▼
Load UserDetails
        │
        ▼
SecurityContextHolder
        │
        ▼
Controller
```

---

## Componentes de seguridad

La capa de seguridad está compuesta por:

```
AuthenticationController
AuthenticationService
CustomUserDetailsService
JwtService
JwtAuthenticationFilter
SecurityConfig
```

---

## Contenido del JWT

El token JWT contiene:

```
username
roles
permissions
expiration
```

Esto permite que el sistema realice autorización sin consultar la base de datos en cada request.

---

## Autorización basada en roles y permisos

El sistema utiliza dos niveles de autorización.

### Roles

Ejemplo:

```
ROLE_ADMIN
ROLE_MANAGER
```

### Permisos

Ejemplo:

```
USER_MANAGE
ORDER_CREATE
PAYMENT_REGISTER
```

Esto permite proteger endpoints mediante anotaciones como:

```java
@PreAuthorize("hasRole('ADMIN')")
```

o

```java
@PreAuthorize("hasAuthority('ORDER_CREATE')")
```

---

## Ventajas del enfoque JWT

Este modelo proporciona:

* autenticación stateless
* mejor escalabilidad
* menor carga en base de datos
* compatibilidad con microservicios

---

# 14. CI/CD

El proyecto está diseñado para integrarse con:

```text
Jenkins
SonarQube
Docker
```

Pipeline esperado:

```text
Pull Request
      ↓
Jenkins Build
      ↓
Tests
      ↓
SonarQube Analysis
      ↓
Merge
```

---

# 15. Objetivo del proyecto

El objetivo de este proyecto es simular el desarrollo de un sistema empresarial completo aplicando:

* arquitectura moderna
* buenas prácticas de ingeniería
* integración de herramientas profesionales
* diseño modular escalable

El sistema comienza como **Monolito Modular** y está preparado para evolucionar hacia **Arquitectura de Microservicios**.

---

## Guarda el documento

Crea:

```text
/docs/architecture.md
```

---

## Commit recomendado

```bash
git add docs/architecture.md
git commit -m "docs: add system architecture documentation"
git push origin develop
```

