
# 🔐 Security Flow — Enterprise Commerce Platform

## 📌 Descripción

Este documento describe el flujo completo de seguridad del sistema, incluyendo:

* autenticación (login)
* validación de JWT
* construcción del contexto de seguridad
* autorización por permisos
* manejo de errores

Este documento reemplaza la documentación previa de:

* arquitectura
* componentes
* filter chain

---

# 🧱 Componentes involucrados

## app (runtime security)

* `SecurityConfig` → configuración de seguridad
* `JwtAuthenticationFilter` → intercepta requests
* `JwtService` → maneja JWT
* `AuthenticationService` → login
* `CustomUserDetailsService` → carga usuario
* `AuthenticationController` → endpoint de login

---

## identity-access

* `User` → usuario
* `Role` → roles
* `Permission` → permisos

---

## common

* `ApiResponseFactory` → respuestas estándar
* excepciones → manejo de errores

---

# 🔐 Flujo de autenticación (login)

1. Cliente envía credenciales:

```
POST /auth/login
```

2. `AuthenticationController` recibe request
3. `AuthenticationService` valida credenciales usando Spring Security
4. Si son válidas:

    * `JwtService` genera token
5. Se devuelve el JWT al cliente

---

# 🔁 Flujo de request autenticado

1. Cliente envía:

```
Authorization: Bearer <token>
```

2. `JwtAuthenticationFilter` intercepta la request

3. El filtro ejecuta:

* valida que el header exista
* valida formato Bearer
* extrae el token

4. `JwtService`:

* extrae username
* valida token (firma + expiración)

5. `CustomUserDetailsService`:

* carga usuario desde `identity-access`
* obtiene roles y permisos
* construye `UserDetails`

6. Se construye autenticación:

* `UsernamePasswordAuthenticationToken`

7. Se registra en:

```
SecurityContextHolder
```

👉 A partir de aquí, la request está autenticada

---

# 🔑 Construcción de authorities

Las authorities se generan a partir de:

* roles → `ROLE_ADMIN`
* permisos → `PAYMENT_CREATE`, `ORDER_CONFIRM`

Ambos se combinan en:

```
CustomUserDetailsService
```

Estas authorities son usadas por Spring Security para autorización.

---

# 🔒 Flujo de autorización

1. Spring Security evalúa:

```
@PreAuthorize("hasAuthority('PAYMENT_CREATE')")
```

2. Si el usuario tiene permiso:

* la request continúa

3. Si no:

* se devuelve **403 Forbidden**

---

# 🔁 Flujo completo

![flujo_completo_security.png](images%2Fflujo_completo_security.png)

---

# 🔐 Flujo de validación de seguridad

![flujo_valid_security.png](images%2Fflujo_valid_security.png)

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

### JWT stateless

* no se usan sesiones
* cada request es independiente

### Separación por módulos

* seguridad en `app`
* identidad en `identity-access`
* utilidades en `common`

### Permisos sobre roles

* mayor granularidad
* más flexible que roles simples

### Manejo explícito de errores

* evita 500 innecesarios
* respuestas consistentes

### Superficie pública reducida

* Actuator restringido
* Swagger controlado

---

# 📌 Resumen

```
Login → JWT → Filtro → SecurityContext → Autorización → Controller
```
