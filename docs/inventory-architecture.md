# Inventory Module — Arquitectura y Funcionamiento

## 1. Introducción

El módulo **Inventory** es responsable de gestionar el stock de productos dentro de la plataforma de comercio.

Sus responsabilidades principales son:

* Mantener el stock disponible por **producto y sucursal**
* Registrar **movimientos de inventario**
* Controlar **reservas de stock para pedidos**
* Permitir **ajustes e ingresos de inventario**
* Proveer información de **stock disponible**

Este módulo forma parte de una arquitectura **monolito modular**, diseñada para evolucionar a microservicios.

---

# 2. Conceptos principales del inventario

El sistema utiliza tres conceptos fundamentales:

### Stock total

Cantidad total de unidades disponibles físicamente.

```
totalQuantity
```

### Stock reservado

Cantidad de unidades comprometidas para pedidos aún no confirmados.

```
reservedQuantity
```

### Stock disponible

Cantidad que realmente puede venderse.

```
available = totalQuantity - reservedQuantity
```

Ejemplo:

```
totalQuantity = 100
reservedQuantity = 30

available = 70
```

Esto evita vender productos que ya están reservados para otros pedidos.

---

# 3. Entidades del módulo Inventory

El modelo actual utiliza dos entidades principales.

```
InventoryItem
StockMovement
```

---

# 3.1 InventoryItem

Representa el **stock actual de un producto en una sucursal específica**.

Tabla:

```
inventory_items
```

Campos principales:

| Campo             | Descripción                               |
| ----------------- | ----------------------------------------- |
| id                | Identificador del registro                |
| branch_id         | Sucursal donde se encuentra el inventario |
| product_id        | Producto asociado                         |
| total_quantity    | Cantidad total en inventario              |
| reserved_quantity | Cantidad reservada para pedidos           |
| active            | Indica si el inventario está activo       |
| created_at        | Fecha de creación                         |
| updated_at        | Fecha de actualización                    |

Restricción importante:

```
(branch_id, product_id) debe ser único
```

Esto significa:

```
un producto solo puede tener un inventario por sucursal
```

Ejemplo:

| branch_id | product_id | total | reserved |
| --------- | ---------- | ----- | -------- |
| 1         | 10         | 100   | 20       |

---

# 3.2 StockMovement

Representa **cada cambio ocurrido en el inventario**.

Tabla:

```
stock_movements
```

Campos principales:

| Campo             | Descripción             |
| ----------------- | ----------------------- |
| id                | Identificador           |
| inventory_item_id | Inventario afectado     |
| movement_type     | Tipo de movimiento      |
| quantity          | Cantidad del movimiento |
| reference         | Referencia externa      |
| notes             | Notas                   |
| created_at        | Fecha                   |

Esta tabla permite:

* auditoría
* trazabilidad
* reconstrucción histórica

---

# 4. Tipos de movimiento de inventario

Los movimientos están definidos por el enum:

```
StockMovementType
```

Tipos:

| Tipo           | Descripción           |
| -------------- | --------------------- |
| INBOUND        | ingreso de stock      |
| OUTBOUND       | salida de stock       |
| ADJUSTMENT_IN  | ajuste positivo       |
| ADJUSTMENT_OUT | ajuste negativo       |
| RESERVATION    | reserva de stock      |
| RELEASE        | liberación de reserva |

---

# 5. Procesos principales del inventario

El inventario participa en varios procesos del sistema.

---

# 5.1 Creación de inventario

Antes de registrar movimientos debe existir un registro de inventario.

Proceso:

```
CreateInventoryItemRequest
        ↓
InventoryService.createInventoryItem()
        ↓
inventory_items
```

Ejemplo:

Sucursal 1 recibe inventario para producto 10.

```
branchId = 1
productId = 10
totalQuantity = 100
```

Resultado:

```
InventoryItem
total = 100
reserved = 0
```

---

# 5.2 Ingreso de stock

Se usa cuando llegan nuevos productos al almacén.

Proceso:

```
InventoryService.inboundStock()
```

Operación:

```
totalQuantity += quantity
```

Ejemplo:

```
stock actual = 100
ingreso = 50
nuevo total = 150
```

Se registra:

```
StockMovement(INBOUND)
```

---

# 5.3 Ajuste de inventario

Se usa cuando hay diferencias entre inventario físico y sistema.

Tipos:

### Ajuste positivo

```
adjustmentIn()
```

Ejemplo:

```
stock = 100
ajuste = +10
nuevo total = 110
```

Movimiento:

```
ADJUSTMENT_IN
```

---

### Ajuste negativo

```
adjustmentOut()
```

Ejemplo:

```
stock = 100
ajuste = -5
nuevo total = 95
```

Movimiento:

```
ADJUSTMENT_OUT
```

El sistema evita bajar el stock por debajo del reservado.

---

# 5.4 Reserva de stock

Cuando se crea un pedido:

```
orders
      ↓
inventory
```

Se incrementa:

```
reservedQuantity
```

Ejemplo:

```
total = 100
reserved = 20
```

Pedido nuevo:

```
cantidad = 10
```

Resultado:

```
reserved = 30
available = 70
```

Movimiento:

```
RESERVATION
```

---

# 5.5 Liberación de reserva

Si un pedido se cancela:

```
reservedQuantity -= cantidad
```

Movimiento:

```
RELEASE
```

Ejemplo:

```
reserved = 30
cancelación = 10
reserved = 20
```

---

# 5.6 Confirmación de venta

Cuando el pedido se confirma y se envía:

```
totalQuantity -= quantity
reservedQuantity -= quantity
```

Movimiento:

```
OUTBOUND
```

---

# 6. Relación con otros módulos

El módulo Inventory interactúa con otros módulos de la plataforma.

```
catalog
orders
branches
```

---

## Catalog

El inventario guarda solo:

```
productId
```

No se guarda la entidad `Product`.

Esto reduce acoplamiento entre módulos.

Si se necesita información del producto:

```
inventory → catalog
```

---

## Branches

El inventario también guarda solo:

```
branchId
```

Cada sucursal mantiene su propio inventario.

---

## Orders

Cuando se crea un pedido:

```
orders → inventory
```

Para:

* verificar stock disponible
* reservar stock

---

# 7. Flujo completo con pedidos

Ejemplo de compra:

```
Cliente compra producto
        ↓
OrderService
        ↓
Catalog obtiene precio
        ↓
Inventory verifica stock
        ↓
Inventory reserva stock
        ↓
Pedido creado
```

Más adelante:

```
Pago confirmado
        ↓
Inventory descuenta stock
```

---

# 8. Ventajas del modelo usado

Este modelo tiene varias ventajas:

### Auditoría completa

Todos los cambios quedan registrados en:

```
stock_movements
```

---

### Control de ventas concurrentes

La separación entre:

```
total
reserved
```

evita vender más unidades de las disponibles.

---

### Preparado para microservicios

El inventario usa referencias:

```
productId
branchId
```

en lugar de relaciones JPA directas.

Esto reduce el acoplamiento entre módulos.

---

### Trazabilidad

Es posible saber:

* cuándo ingresó stock
* quién lo modificó
* por qué ocurrió el movimiento

---

# 9. Evolución futura del módulo

En fases posteriores se agregarán:

### StockReservation

Para manejar reservas más complejas.

### Integración directa con Orders

Para reservar stock automáticamente.

### Eventos de inventario

Con RabbitMQ para:

```
inventory.low
inventory.reserved
inventory.released
```

---

# 10. Resumen

El módulo Inventory:

* gestiona stock por producto y sucursal
* mantiene historial de movimientos
* controla reservas para pedidos
* evita sobreventa
* mantiene bajo acoplamiento con otros módulos

Esto lo convierte en una base sólida para un sistema de comercio empresarial.
