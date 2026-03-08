# Enterprise Commerce Platform

## Development Workflow & Git Strategy

Este documento describe las reglas de desarrollo, flujo de trabajo con Git, convenciones de commits y manejo de ramas para el proyecto **Enterprise Commerce Platform**.

El objetivo es mantener un desarrollo ordenado, escalable y alineado con prácticas utilizadas en equipos de ingeniería profesionales.

---

# 1. Estrategia de ramas (Branching Strategy)

El proyecto utiliza un modelo simplificado basado en **GitFlow**.

## Ramas principales

```
main
develop
feature/*
fix/*
refactor/*
chore/*
```

### main

* Contiene código **estable listo para producción**.
* Solo recibe cambios mediante **Pull Request desde `develop`**.

### develop

* Rama principal de integración de desarrollo.
* Todas las funcionalidades nuevas se integran aquí mediante **Pull Request**.

---

# 2. Ramas de trabajo

## feature/*

Se utilizan para desarrollar nuevas funcionalidades.

Ejemplos:

```
feature/identity-access-services
feature/jwt-authentication
feature/order-module
feature/inventory-management
```

Flujo:

```
develop
   ↓
feature/nueva-funcionalidad
   ↓
Pull Request
   ↓
develop
```

---

## fix/*

Correcciones de errores.

Ejemplo:

```
fix/security-filter
fix/order-validation
```

---

## refactor/*

Refactorización sin cambiar comportamiento funcional.

Ejemplo:

```
refactor/common-exception-handling
refactor/order-service
```

---

## chore/*

Tareas de mantenimiento o infraestructura.

Ejemplo:

```
chore/gradle-upgrade
chore/docker-setup
```

---

# 3. Flujo de trabajo con Git

## 1 Crear rama feature

Siempre partir desde `develop`.

```bash
git checkout develop
git pull origin develop
git checkout -b feature/nombre-feature
```

---

## 2 Desarrollar normalmente

```bash
git add .
git commit -m "feat: add identity access services"
```

---

## 3 Subir rama al repositorio remoto

```bash
git push origin feature/nombre-feature
```

---

## 4 Crear Pull Request

El Pull Request debe apuntar a:

```
feature/*  →  develop
```

---

## 5 Revisar cambios

Antes de hacer merge se revisa:

* compilación
* estilo de código
* arquitectura
* tests
* análisis de calidad

En el futuro esto se integrará con:

* **Jenkins CI**
* **SonarQube**

---

## 6 Merge del Pull Request

Una vez aprobado:

```
feature → develop
```

Después la rama feature se elimina.

---

# 4. Releases

Cuando la rama `develop` esté estable:

```
develop → main
```

mediante Pull Request.

La rama `main` representa versiones estables del sistema.

---

# 5. Convención de commits

El proyecto usa **Conventional Commits**.

Formato:

```
tipo: descripción breve
```

## Tipos permitidos

### feat

Nueva funcionalidad.

Ejemplo:

```
feat: add identity access entities
```

---

### fix

Corrección de bug.

Ejemplo:

```
fix: correct flyway migration
```

---

### refactor

Refactorización de código sin cambiar funcionalidad.

Ejemplo:

```
refactor: improve api response factory
```

---

### chore

Cambios de infraestructura o mantenimiento.

Ejemplo:

```
chore: update gradle dependencies
```

---

### docs

Cambios en documentación.

Ejemplo:

```
docs: add development workflow guide
```

---

### test

Adición o modificación de tests.

Ejemplo:

```
test: add user service unit tests
```

---

# 6. Pull Requests

Cada Pull Request debe incluir:

* descripción clara del cambio
* referencia a la feature o issue
* explicación de decisiones importantes

Ejemplo:

```
Feature: Identity Access Services

Includes:
- UserService
- RoleService
- PermissionService
- Repository queries

Purpose:
Prepare foundation for authentication module.
```

---

# 7. Reglas de calidad del código

Antes de aceptar un Pull Request se recomienda verificar:

* el proyecto **compila**
* no existen warnings críticos
* los tests pasan
* se respetan las reglas de arquitectura

En el futuro se integrará con:

```
Jenkins CI
SonarQube
Testcontainers
```

para automatizar estas validaciones.

---

# 8. Estructura general del proyecto

Arquitectura actual del proyecto:

```
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

Cada módulo representa un **bounded context** dentro de la plataforma.

El sistema comienza como **Monolito Modular** con posibilidad de evolucionar hacia **microservicios**.

---

# 9. Principios de arquitectura

El proyecto sigue estos principios:

* separación clara por módulos
* arquitectura en capas
* dominio aislado
* repositorios encapsulados
* DTOs para APIs
* manejo uniforme de errores
* trazabilidad mediante `traceId`
* migraciones controladas con **Flyway**

---

# 10. Herramientas del proyecto

Tecnologías utilizadas:

Backend:

```
Java 21
Spring Boot 3
Spring Security
Spring Data JPA
```

Infraestructura:

```
PostgreSQL
RabbitMQ
Docker
```

Calidad y CI/CD:

```
Jenkins
SonarQube
Testcontainers
```

Documentación:

```
Swagger / OpenAPI
```

---

# 11. Futuras integraciones

Durante el desarrollo se agregarán:

* JWT Authentication
* API Gateway
* Event-driven communication (RabbitMQ)
* Observabilidad
* CI/CD completo

---

# 12. Objetivo del proyecto

Este proyecto simula el desarrollo de un sistema empresarial completo tipo **Enterprise Commerce Platform**, aplicando prácticas utilizadas en organizaciones grandes.

El propósito es:

* practicar arquitectura empresarial
* aplicar patrones modernos
* integrar herramientas de calidad
* construir una base escalable
* evolucionar de **Monolito Modular → Microservicios**

---

---

## Dónde guardarlo

Crea esta estructura en tu repo:

```
enterprise-commerce-platform
│
├─ docs
│  └─ development-workflow.md
```

---

## Commit recomendado

```
git add docs/development-workflow.md
git commit -m "docs: add development workflow and git strategy"
git push origin develop
```

