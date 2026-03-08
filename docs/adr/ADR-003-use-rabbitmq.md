# ADR-003 — Use RabbitMQ for Event Driven Communication

## Status

Accepted

## Context

As the platform grows, modules will need to communicate asynchronously.

Examples include:

* order creation triggering inventory updates
* payment confirmation triggering notifications
* stock adjustments triggering alerts

Synchronous communication between modules can lead to:

* tight coupling
* reduced scalability
* cascading failures

Event-driven architecture helps decouple modules.

Possible technologies considered:

1. Apache Kafka
2. RabbitMQ
3. In-memory event system

## Decision

The system will use **RabbitMQ** as the message broker for asynchronous communication.

RabbitMQ was chosen because:

* it is simpler to operate than Kafka for this scale
* integrates easily with Spring Boot
* supports reliable message delivery
* supports flexible routing patterns

Example events:

```text
OrderCreatedEvent
PaymentCompletedEvent
InventoryAdjustedEvent
CustomerRegisteredEvent
```

## Consequences

Advantages:

* decoupled modules
* better scalability
* improved system resilience

Disadvantages:

* additional infrastructure
* eventual consistency must be handled

## Future Considerations

If event throughput grows significantly, the system may evolve to use Kafka or a hybrid architecture.
