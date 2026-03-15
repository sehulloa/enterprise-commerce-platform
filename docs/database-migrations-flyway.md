# Migraciones de Base de Datos con Flyway

## Visión general

Este proyecto utiliza **Flyway** para gestionar los cambios en el esquema de la base de datos de forma **controlada, versionada y reproducible**.

Flyway permite que la estructura de la base de datos evolucione junto con el código de la aplicación, manteniendo un historial completo de todos los cambios.

En lugar de permitir que Hibernate/JPA cree o modifique automáticamente las tablas de la base de datos, **todos los cambios de esquema se definen explícitamente mediante scripts SQL versionados**.

Este enfoque es una práctica estándar en sistemas empresariales porque garantiza:

* estructura de base de datos determinística
* entornos reproducibles
* despliegues controlados
* auditoría de cambios en el esquema

---

# Por qué se utiliza Flyway

Aunque Hibernate puede generar tablas automáticamente a partir de entidades JPA, depender de ese mecanismo en entornos de producción introduce varios riesgos.

La generación automática de esquema con Hibernate:

* oculta el SQL que se ejecuta
* no mantiene historial de migraciones
* puede generar inconsistencias entre entornos
* dificulta revertir cambios

Flyway soluciona estos problemas obligando a que **cada cambio en la base de datos se defina como un script de migración versionado**.

---

# Cómo funciona Flyway

Cuando la aplicación inicia, Flyway se ejecuta **antes de Hibernate**.

Orden de arranque:

```text
Inicio de la aplicación
        ↓
Flyway ejecuta migraciones pendientes
        ↓
Hibernate valida el mapeo de entidades
        ↓
La aplicación queda lista
```

Flyway verifica qué migraciones ya se ejecutaron utilizando una tabla especial:

```
flyway_schema_history
```

Esta tabla almacena el historial de todas las migraciones aplicadas.

Si Flyway detecta scripts nuevos que aún no se han ejecutado, los aplica automáticamente.

---

# Archivos de migración de Flyway

Todos los scripts de migración se almacenan en:

```
app/src/main/resources/db/migration
```

Los archivos de migración siguen esta convención de nombres:

```
V{version}__{descripcion}.sql
```

Ejemplos:

```
V1__init.sql
V2__create_identity_access_tables.sql
V3__seed_identity_access_data.sql
V4__create_orders_tables.sql
V5__create_catalog_tables.sql
```

Reglas importantes:

* Los números de versión deben aumentar secuencialmente
* Los scripts no deben modificarse después de ejecutarse
* Cada migración representa un cambio lógico en la base de datos

---

# La tabla flyway_schema_history

Flyway crea automáticamente una tabla llamada:

```
flyway_schema_history
```

Esta tabla registra todas las migraciones que se han ejecutado.

Ejemplo de contenido:

| installed_rank | version | description                   | script                                |
| -------------- | ------- | ----------------------------- | ------------------------------------- |
| 1              | 1       | init                          | V1__init.sql                          |
| 2              | 2       | create_identity_access_tables | V2__create_identity_access_tables.sql |
| 3              | 3       | seed_identity_access_data     | V3__seed_identity_access_data.sql     |
| 4              | 4       | create_orders_tables          | V4__create_orders_tables.sql          |
| 5              | 5       | create_catalog_tables         | V5__create_catalog_tables.sql         |

Para consultar el historial de migraciones:

```sql
SELECT version, description, script
FROM flyway_schema_history
ORDER BY installed_rank;
```

---

# Cambios comunes en base de datos con Flyway

Las migraciones Flyway se utilizan para **cualquier modificación estructural en la base de datos**.

Ejemplos comunes:

### Crear tablas

```sql
CREATE TABLE products (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);
```

### Modificar tablas

```sql
ALTER TABLE products
ADD COLUMN weight NUMERIC(10,2);
```

### Crear índices

```sql
CREATE INDEX idx_products_sku
ON products(sku);
```

### Agregar claves foráneas

```sql
ALTER TABLE orders
ADD CONSTRAINT fk_orders_customer
FOREIGN KEY (customer_id)
REFERENCES customers(id);
```

### Insertar datos base

```sql
INSERT INTO roles(name)
VALUES ('ADMIN');
```

Esto se conoce como **data seeding**.

---

# Responsabilidades de Hibernate vs Flyway

En este proyecto, las responsabilidades están separadas de la siguiente manera:

```
Flyway
    gestiona la estructura de la base de datos

Hibernate
    mapea entidades a tablas y valida el esquema
```

Configuración típica:

```
spring.jpa.hibernate.ddl-auto=validate
```

Esto significa:

* Hibernate **no crea tablas**
* Hibernate **solo verifica que el esquema coincida con las entidades**

Flyway es responsable de crear y modificar la estructura de la base de datos.

---

# Flujo de trabajo para migraciones

Cuando se necesita modificar el esquema de la base de datos, se sigue el siguiente proceso.

### Paso 1

Crear un nuevo archivo de migración.

Ejemplo:

```
V6__add_product_weight.sql
```

### Paso 2

Agregar el cambio SQL correspondiente.

```sql
ALTER TABLE products
ADD COLUMN weight NUMERIC(10,2);
```

### Paso 3

Ejecutar la aplicación.

Flyway detectará automáticamente la nueva migración y la ejecutará.

---

# Regla importante de Flyway

Una vez que una migración ha sido ejecutada, **nunca debe modificarse**.

Incorrecto:

```
editar V5__create_catalog_tables.sql
```

Correcto:

```
crear V6__modify_catalog_tables.sql
```

Esto garantiza que todos los entornos permanezcan consistentes.

---

# Consistencia entre entornos

Uno de los beneficios más importantes de Flyway es la reproducibilidad.

Una base de datos puede reconstruirse completamente ejecutando todas las migraciones en orden:

```
V1
V2
V3
V4
V5
...
```

Esto permite:

* entornos de desarrollo consistentes
* pipelines CI/CD confiables
* despliegues de producción predecibles

---

# Resumen

Flyway proporciona:

* migraciones de base de datos versionadas
* entornos reproducibles
* evolución controlada del esquema
* historial completo de cambios
* despliegues más seguros

En este proyecto, Flyway es la **fuente única de verdad para la estructura de la base de datos**.

Todos los cambios en el esquema deben implementarse mediante scripts de migración Flyway.
