## Security Components Blueprint

Este documento describe los componentes que implementarán la autenticación y autorización del sistema utilizando **Spring Security + JWT**.

El objetivo es definir claramente las responsabilidades de cada clase antes de implementar la seguridad.

---

# 1. Componentes de la capa de seguridad

Los componentes principales que implementaremos son:

```text
AuthenticationController
CustomUserDetailsService
JwtService
JwtAuthenticationFilter
SecurityConfig
AuthenticationService
```

Cada componente tiene una responsabilidad específica dentro del flujo de autenticación.

---

# 2. Diagrama general de componentes

```text
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

# 3. Flujo de autenticación

## Paso 1 — Cliente envía credenciales

El cliente envía un request al endpoint de login:

```text
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

## Paso 2 — AuthenticationController

Responsabilidad:

* recibir la solicitud de login
* delegar autenticación al servicio correspondiente

Ejemplo conceptual:

```java
@PostMapping("/auth/login")
public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
    return ResponseEntity.ok(authenticationService.authenticate(request));
}
```

---

## Paso 3 — AuthenticationService

Responsabilidad:

* autenticar credenciales
* generar token JWT

Flujo interno:

```text
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
```

---

## Paso 4 — CustomUserDetailsService

Este componente carga el usuario desde la base de datos.

Responsabilidades:

* buscar usuario
* cargar roles
* cargar permisos
* construir `UserDetails`

Ejemplo conceptual:

```java
UserDetails loadUserByUsername(String username)
```

---

# 4. Generación del token

Después de validar credenciales, el sistema genera un token.

```text
JwtService
```

Responsabilidades:

* generar token
* validar token
* extraer username
* verificar expiración

Ejemplo conceptual:

```java
String generateToken(UserDetails userDetails);
```

---

# 5. Flujo de autorización

Después del login, cada request del cliente incluirá el token.

Ejemplo:

```text
Authorization: Bearer <JWT>
```

Flujo:

```text
Client Request
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
SecurityContext
       │
       ▼
Controller
```

---

# 6. Diagrama del flujo de autorización

```text
        Client Request
Authorization: Bearer <JWT>
              │
              ▼
      JwtAuthenticationFilter
              │
              ▼
         JwtService
              │
              ▼
      Validate Token
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

# 7. SecurityConfig

Este componente configura Spring Security.

Responsabilidades:

* definir endpoints públicos
* registrar filtros
* configurar autenticación
* desactivar sesiones

Ejemplo conceptual:

```java
@Bean
SecurityFilterChain filterChain(HttpSecurity http)
```

Configuraciones importantes:

```text
disable CSRF
stateless session
register JWT filter
define protected endpoints
```

---

# 8. Orden de ejecución

Los componentes se ejecutan en este orden:

```text
Client Request
      │
      ▼
JwtAuthenticationFilter
      │
      ▼
SecurityContextHolder
      │
      ▼
Controller
```

Esto permite que el controlador se ejecute con el usuario autenticado.

---

# 9. Autoridades del usuario

Las autoridades del usuario se construirán a partir de:

```text
Roles
Permissions
```

Ejemplo final:

```text
ROLE_ADMIN
USER_MANAGE
ORDER_CREATE
PAYMENT_REGISTER
```

Estas autoridades permiten usar anotaciones como:

```java
@PreAuthorize("hasRole('ADMIN')")
```

o

```java
@PreAuthorize("hasAuthority('ORDER_CREATE')")
```

---

# 10. Ventajas del diseño

Este diseño permite:

* autenticación stateless
* seguridad escalable
* compatibilidad con microservicios
* autorización granular por permisos

---

# 11. Componentes que implementaremos en FASE 4.3

```text
CustomUserDetailsService
JwtService
JwtAuthenticationFilter
AuthenticationService
AuthenticationController
SecurityConfig
```

Estos componentes completarán la capa de seguridad del sistema.
