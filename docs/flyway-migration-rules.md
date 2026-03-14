# Reglas para escribir migraciones Flyway

## Propósito

Este documento define las reglas y buenas prácticas para crear migraciones de base de datos utilizando **Flyway** en este proyecto.

El objetivo es garantizar:

* consistencia entre entornos
* despliegues seguros
* historial confiable de cambios
* facilidad para reconstruir la base de datos

Todas las modificaciones al esquema de la base de datos deben realizarse **exclusivamente mediante migraciones Flyway**.

---

# Ubicación de las migraciones

Todas las migraciones se almacenan en:

```
app/src/main/resources/db/migration
```

Flyway detecta automáticamente los archivos en este directorio.

---

# Convención de nombres

Los archivos deben seguir el formato:

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
V6__add_product_weight.sql
```

### Reglas

* La versión debe comenzar con **V**
* La versión debe ser incremental
* Usar **doble guion bajo `__`** entre versión y descripción
* Usar **snake_case** para la descripción
* No usar espacios en el nombre del archivo

---

# Regla más importante

Una migración **nunca debe modificarse después de haber sido ejecutada**.

Ejemplo incorrecto:

```
Editar V5__create_catalog_tables.sql
```

Ejemplo correcto:

```
Crear V6__fix_catalog_tables.sql
```

Modificar migraciones ya ejecutadas puede provocar inconsistencias entre entornos.

---

# Orden de ejecución

Flyway ejecuta las migraciones en orden de versión.

Ejemplo:

```
V1
V2
V3
V4
V5
V6
```

Cada migración se ejecuta **una sola vez**.

El historial se almacena en la tabla:

```
flyway_schema_history
```

---

# Tipos de migraciones comunes

## Crear tablas

Ejemplo:

```sql
CREATE TABLE products (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);
```

---

## Modificar tablas

Ejemplo:

```sql
ALTER TABLE products
ADD COLUMN weight NUMERIC(10,2);
```

---

## Crear índices

Ejemplo:

```sql
CREATE INDEX idx_products_sku
ON products(sku);
```

---

## Agregar claves foráneas

Ejemplo:

```sql
ALTER TABLE orders
ADD CONSTRAINT fk_orders_customer
FOREIGN KEY (customer_id)
REFERENCES customers(id);
```

---

## Insertar datos iniciales (data seeding)

Ejemplo:

```sql
INSERT INTO roles(name)
VALUES ('ADMIN');
```

Esto se usa para cargar datos base del sistema.

---

# Buenas prácticas

## 1. Una migración = un cambio lógico

Cada archivo debe representar **un cambio específico** en el esquema.

Incorrecto:

```
V10__many_changes.sql
```

Correcto:

```
V10__add_product_weight.sql
V11__add_product_index.sql
```

---

## 2. Usar nombres descriptivos

La descripción debe indicar claramente qué hace la migración.

Ejemplo bueno:

```
V7__add_order_currency_column.sql
```

Ejemplo malo:

```
V7__update.sql
```

---

## 3. Incluir constraints explícitamente

Siempre que sea posible, definir:

* claves primarias
* claves foráneas
* índices

Ejemplo:

```sql
CONSTRAINT fk_orders_customer
FOREIGN KEY (customer_id)
REFERENCES customers(id)
```

---

## 4. Evitar eliminar columnas en producción

Eliminar columnas puede romper versiones anteriores del sistema.

En lugar de eliminar:

```
DROP COLUMN
```

se recomienda:

* marcar como obsoleta
* eliminar en una migración futura cuando sea seguro

---

## 5. Probar migraciones en base limpia

Antes de hacer commit, es recomendable probar que las migraciones funcionan desde cero:

1. eliminar base de datos local
2. crear base nueva
3. ejecutar la aplicación

Flyway debe aplicar todas las migraciones correctamente.

---

# Flujo de trabajo recomendado

Cuando se requiere un cambio en la base de datos:

### Paso 1

Crear una nueva migración.

Ejemplo:

```
V6__add_product_weight.sql
```

### Paso 2

Agregar el cambio SQL.

```
ALTER TABLE products
ADD COLUMN weight NUMERIC(10,2);
```

### Paso 3

Ejecutar la aplicación.

Flyway aplicará la nueva migración automáticamente.

### Paso 4

Confirmar que la migración aparece en:

```
flyway_schema_history
```

---

# Responsabilidades

En este proyecto:

```
Flyway
    controla la estructura de la base de datos

Hibernate
    mapea entidades y valida el esquema
```

Configuración recomendada:

```
spring.jpa.hibernate.ddl-auto=validate
```

Esto asegura que la estructura definida por Flyway coincide con las entidades JPA.

---

# Resumen

Para trabajar correctamente con Flyway en este proyecto:

* todas las modificaciones de base de datos deben hacerse mediante migraciones
* nunca modificar migraciones ya ejecutadas
* usar nombres claros y versionado incremental
* probar migraciones antes de hacer commit
* mantener consistencia entre entornos

Seguir estas reglas garantiza un historial de migraciones confiable y despliegues seguros.
