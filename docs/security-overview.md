
# 🔐 Security Overview — Enterprise Commerce Platform

## 📌 Descripción

Este documento describe la arquitectura de seguridad del sistema, sus componentes principales, responsabilidades y decisiones de diseño.

La implementación está basada en:

* autenticación stateless con JWT
* autorización basada en permisos
* integración con Spring Security

---

## 📌 Alcance

Incluye:

* autenticación con JWT
* autorización por permisos
* configuración de seguridad
* integración con el módulo de identidad

No incluye:

* refresh tokens
* OAuth2 / SSO
* revocación de tokens

Estas capacidades pueden incorporarse en fases futuras.

---

# 🧱 Arquitectura de seguridad

La seguridad del sistema está distribuida en tres módulos principales:

## app (runtime de seguridad)

Responsable de la ejecución de la seguridad:

* configuración de Spring Security
* filtros de autenticación
* login
* integración entre componentes

---

## identity-access (identidad)

Responsable de:

* gestión de usuarios
* roles
* permisos

Es la fuente de verdad para autenticación y autorización.

---

## common (soporte compartido)

Responsable de:

* respuestas estándar (`ApiResponse`)
* excepciones (`BusinessException`, etc.)
* utilidades comunes

---

# 🔧 Componentes principales

## SecurityConfig

Ubicación:
`app/security/config/SecurityConfig.java`

Responsabilidad:

* configurar Spring Security
* definir endpoints públicos
* aplicar seguridad stateless
* registrar `JwtAuthenticationFilter`
* habilitar autorización con `@PreAuthorize`

---

## JwtAuthenticationFilter

Ubicación:
`app/security/jwt/JwtAuthenticationFilter.java`

Responsabilidad:

* interceptar cada request
* extraer token JWT
* validar token
* autenticar usuario

Es el punto central del flujo de seguridad en runtime.

---

## JwtService

Ubicación:
`app/security/jwt/JwtService.java`

Responsabilidad:

* generar tokens JWT
* validar tokens
* extraer información (username, claims)

---

## AuthenticationService

Ubicación:
`app/security/service/AuthenticationService.java`

Responsabilidad:

* validar credenciales
* generar token JWT

---

## CustomUserDetailsService

Ubicación:
`app/security/service/CustomUserDetailsService.java`

Responsabilidad:

* cargar usuario desde `identity-access`
* obtener roles y permisos
* construir `UserDetails`

---

## AuthenticationController

Ubicación:
`app/security/controller/AuthenticationController.java`

Responsabilidad:

* exponer endpoint de autenticación

Endpoint:

POST /auth/login

---

# 🔗 Interacción entre componentes

El flujo interno se conecta de la siguiente forma:

* `AuthenticationController` → `AuthenticationService` → `JwtService`
* `JwtAuthenticationFilter` → `JwtService` → `CustomUserDetailsService`
* `CustomUserDetailsService` → `identity-access`

Esto permite separar claramente:

* autenticación
* validación de token
* carga de usuario
* autorización

---

# 🔑 Modelo de autorización

El sistema utiliza:

* Roles → `ROLE_ADMIN`
* Permisos → `PAYMENT_CREATE`, `ORDER_CONFIRM`, etc.

Las authorities se construyen combinando ambos en:

`CustomUserDetailsService`

---

## Uso en endpoints

La autorización se define con:

@PreAuthorize("hasAuthority('PAYMENT_CREATE')")

Esto permite:

* control granular
* independencia de roles
* mayor flexibilidad

---

# ⚠️ Manejo de errores

| Caso           | Código | Descripción             |
| -------------- | ------ | ----------------------- |
| Sin token      | 401    | Authentication required |
| Token inválido | 401    | Invalid token           |
| Token expirado | 401    | Token expired           |
| Sin permisos   | 403    | Access denied           |

---

# 🧠 Decisiones de diseño

## JWT stateless

* no se usan sesiones
* cada request es independiente

---

## Separación por módulos

* seguridad en `app`
* identidad en `identity-access`
* utilidades en `common`

---

## Permisos sobre roles

* mayor granularidad
* evita lógica rígida
* más escalable

---

## Manejo explícito de errores

* evita respuestas 500 innecesarias
* mantiene consistencia en la API

---

## Superficie pública reducida

* Actuator restringido
* Swagger controlado
* endpoints públicos mínimos

---

# 📌 Relación con el flujo

Este documento describe la estructura y componentes.

Para ver el comportamiento en ejecución, consultar:

`security-flow.md`

---

# 🎯 Conclusión

La seguridad del sistema proporciona:

* autenticación segura con JWT
* autorización basada en permisos
* arquitectura modular clara
* comportamiento consistente en errores

Esto establece una base sólida y extensible para evolucionar hacia mecanismos más avanzados en futuras fases.
