## Security Architecture (JWT Authentication)

Este documento describe cómo funciona la autenticación y autorización del sistema utilizando **JSON Web Tokens (JWT)**.

El objetivo es implementar un mecanismo de seguridad **stateless**, escalable y compatible con arquitecturas modernas.

---

# 1. Conceptos básicos

El sistema utiliza **JWT (JSON Web Token)** para autenticar usuarios.

Un JWT es un token firmado que contiene información del usuario autenticado.

Ejemplo simplificado de token:

```
header.payload.signature
```

El token se envía en cada request dentro del header:

```
Authorization: Bearer <token>
```

Esto permite que el servidor **no tenga que mantener sesiones**.

---

# 2. Componentes de seguridad del sistema

Los componentes principales que implementaremos son:

```
AuthenticationController
CustomUserDetailsService
JwtService
JwtAuthenticationFilter
SecurityConfig
```

Cada uno tiene una responsabilidad específica.

---

# 3. Flujo completo de autenticación

## Paso 1 — Login

El cliente envía credenciales al endpoint:

```
POST /auth/login
```

Ejemplo:

```json
{
  "username": "admin",
  "password": "password"
}
```

---

## Paso 2 — Validación del usuario

El sistema consulta el usuario en base de datos.

```
CustomUserDetailsService
        │
        ▼
UserService
        │
        ▼
UserRepository
        │
        ▼
Database
```

El sistema obtiene:

* usuario
* roles
* permisos

---

## Paso 3 — Generación del JWT

Si las credenciales son correctas:

```
JwtService
    │
    ▼
Genera JWT
```

El token contendrá información como:

```json
{
  "sub": "admin",
  "roles": ["ADMIN"],
  "permissions": ["USER_MANAGE", "ORDER_CREATE"],
  "exp": 1710000000
}
```

El servidor devuelve el token al cliente.

---

## Paso 4 — Cliente guarda el token

El cliente guarda el token.

En cada request posterior envía:

```
Authorization: Bearer <JWT>
```

---

# 4. Flujo de autorización en cada request

Cuando el cliente llama a cualquier endpoint protegido:

```
GET /orders
Authorization: Bearer <JWT>
```

El flujo interno será:

```
Client Request
      │
      ▼
JwtAuthenticationFilter
      │
      ▼
Validate JWT
      │
      ▼
Load Authorities
      │
      ▼
SecurityContext
      │
      ▼
Protected Controller
```

---

# 5. Diagrama del flujo de autenticación

```
        Client
          │
          │ POST /auth/login
          ▼
AuthenticationController
          │
          ▼
AuthenticationManager
          │
          ▼
CustomUserDetailsService
          │
          ▼
UserRepository
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

# 6. Diagrama del flujo de autorización

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
Extract Username
        │
        ▼
Load Authorities
        │
        ▼
SecurityContextHolder
        │
        ▼
Controller
```

---

# 7. Roles y permisos

El sistema maneja dos tipos de autoridades.

## Roles

Identifican grupos de permisos.

Ejemplo:

```
ROLE_ADMIN
ROLE_SALES_AGENT
ROLE_INVENTORY_MANAGER
```

---

## Permisos

Representan acciones específicas.

Ejemplo:

```
USER_MANAGE
ORDER_CREATE
PAYMENT_REGISTER
INVENTORY_ADJUST
```

---

# 8. Conversión a Authorities de Spring Security

Cuando se carga el usuario, los roles y permisos se convierten en:

```
GrantedAuthority
```

Ejemplo:

```
ROLE_ADMIN
USER_MANAGE
ORDER_CREATE
PAYMENT_REGISTER
```

Esto permite usar anotaciones como:

```java
@PreAuthorize("hasRole('ADMIN')")
```

o

```java
@PreAuthorize("hasAuthority('ORDER_CREATE')")
```

---

# 9. Ventajas de usar JWT

El uso de JWT ofrece varias ventajas:

* autenticación **stateless**
* escalabilidad horizontal
* compatibilidad con microservicios
* menor carga en base de datos
* integración sencilla con API Gateway

---

# 10. Consideraciones de seguridad

Para garantizar seguridad se aplicarán las siguientes medidas:

* tokens firmados con **HS256**
* expiración del token
* validación en cada request
* uso de HTTPS
* filtros de seguridad en Spring

---

# 11. Futuras mejoras

En futuras versiones del sistema se podrán agregar:

* refresh tokens
* revocación de tokens
* OAuth2 / OpenID Connect
* integración con Identity Providers externos

---

# 12. Relación con la arquitectura del sistema

La capa de seguridad interactúa con los siguientes módulos:

```
identity-access
common
app
```

Esto permite mantener la lógica de autenticación separada del resto de dominios del sistema.

---

# 13. Objetivo del diseño

Este diseño busca proporcionar:

* autenticación segura
* autorización flexible
* arquitectura escalable
* integración sencilla con microservicios

La implementación comenzará en **FASE 4.3 — Security** del proyecto.
