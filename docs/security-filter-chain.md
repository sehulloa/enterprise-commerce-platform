## Spring Security Filter Chain

Este documento explica cómo funciona el **Security Filter Chain** de Spring Security y cómo se integrará el filtro de autenticación JWT en el sistema.

Comprender este flujo es fundamental para implementar correctamente la autenticación basada en tokens.

---

# 1. ¿Qué es el Security Filter Chain?

Spring Security protege las aplicaciones interceptando las peticiones HTTP mediante una **cadena de filtros**.

Cada request pasa por múltiples filtros antes de llegar al controlador.

```text
Client Request
      │
      ▼
Security Filter Chain
      │
      ▼
Controller
```

Cada filtro tiene una responsabilidad específica como:

* autenticación
* autorización
* manejo de sesiones
* protección CSRF
* validación de tokens

---

# 2. Flujo simplificado de filtros

Un flujo típico de Spring Security se ve así:

```text
Client Request
      │
      ▼
SecurityContextPersistenceFilter
      │
      ▼
UsernamePasswordAuthenticationFilter
      │
      ▼
JwtAuthenticationFilter (nuestro filtro)
      │
      ▼
AuthorizationFilter
      │
      ▼
Controller
```

En nuestro sistema, el filtro más importante será:

```text
JwtAuthenticationFilter
```

---

# 3. Flujo completo de request en nuestro sistema

Cuando el cliente hace una petición protegida:

```text
GET /orders
Authorization: Bearer <JWT>
```

El flujo interno será:

```text
Client Request
      │
      ▼
Security Filter Chain
      │
      ▼
JwtAuthenticationFilter
      │
      ▼
Validar Token
      │
      ▼
Extraer Usuario
      │
      ▼
Cargar Permisos
      │
      ▼
SecurityContextHolder
      │
      ▼
Controller
```

---

# 4. Diagrama del flujo completo

```text
            Client
              │
              │ Authorization: Bearer <JWT>
              ▼
      Security Filter Chain
              │
              ▼
     JwtAuthenticationFilter
              │
              ▼
       Validate JWT
              │
              ▼
      Extract Username
              │
              ▼
     CustomUserDetailsService
              │
              ▼
          Database
              │
              ▼
     Build Authentication
              │
              ▼
      SecurityContextHolder
              │
              ▼
          Controller
```

---

# 5. Qué hace el JwtAuthenticationFilter

Nuestro filtro tendrá estas responsabilidades:

1. Leer el header `Authorization`
2. Extraer el token JWT
3. Validar el token
4. Obtener el username del token
5. Cargar el usuario desde base de datos
6. Construir un `Authentication`
7. Guardarlo en `SecurityContext`

Ejemplo conceptual:

```java
String token = extractToken(request);

if(jwtService.isValid(token)) {

    String username = jwtService.extractUsername(token);

    UserDetails user = userDetailsService.loadUserByUsername(username);

    Authentication authentication = new UsernamePasswordAuthenticationToken(
            user,
            null,
            user.getAuthorities()
    );

    SecurityContextHolder.getContext().setAuthentication(authentication);
}
```

Esto permite que el resto de la aplicación reconozca al usuario autenticado.

---

# 6. SecurityContext

Spring Security guarda la autenticación en:

```text
SecurityContextHolder
```

Esto permite que cualquier parte del sistema pueda obtener el usuario autenticado.

Ejemplo:

```java
Authentication auth = SecurityContextHolder.getContext().getAuthentication();
```

---

# 7. Autorización en controladores

Una vez autenticado el usuario, podemos proteger endpoints con anotaciones.

Ejemplo por rol:

```java
@PreAuthorize("hasRole('ADMIN')")
```

Ejemplo por permiso:

```java
@PreAuthorize("hasAuthority('ORDER_CREATE')")
```

Esto funciona gracias a las **GrantedAuthority** que cargamos en el `UserDetails`.

---

# 8. Ventaja del enfoque JWT

Este enfoque tiene varias ventajas:

* no utiliza sesiones
* escalable horizontalmente
* compatible con microservicios
* desacopla autenticación del servidor

Cada request contiene toda la información necesaria para autenticarse.

---

# 9. Orden de filtros

El `JwtAuthenticationFilter` se registrará antes de:

```text
UsernamePasswordAuthenticationFilter
```

Esto garantiza que el token se procese antes de la autenticación estándar de Spring.

Ejemplo de configuración:

```java
http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
```

---

# 10. Relación con los módulos del sistema

La seguridad interactúa con estos módulos:

```text
identity-access
common
app
```

Roles y permisos provienen del módulo:

```text
identity-access
```

---

# 11. Objetivo del diseño

Este diseño permite:

* autenticación segura
* autorización flexible
* integración con microservicios
* escalabilidad del sistema

---

# 12. Implementación

La implementación de estos componentes se realizará en:

```text
FASE 4.3 — Security
```

Componentes que se desarrollarán:

```text
CustomUserDetailsService
JwtService
JwtAuthenticationFilter
SecurityConfig
AuthenticationController
```
