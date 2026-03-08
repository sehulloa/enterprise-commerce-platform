# ADR-001 — Adopt Modular Monolith Architecture

## Status

Accepted

## Context

The system being developed is an enterprise commerce platform expected to handle several domains such as:

* identity and access
* customers
* catalog
* inventory
* orders
* payments
* notifications

A key architectural decision must be made regarding how the system will be structured initially.

Possible options considered:

1. Start directly with **Microservices**
2. Use a **Traditional Monolith**
3. Use a **Modular Monolith**

Starting with microservices introduces high operational complexity early in the project, including:

* distributed transactions
* network communication
* deployment complexity
* service orchestration

However, a simple monolith may lead to tight coupling and make future scaling difficult.

## Decision

The system will start as a **Modular Monolith**.

Each domain will be implemented as an independent module within a multi-module Gradle project.

Example modules:

* common
* identity-access
* customers
* catalog
* inventory
* orders
* payments
* notifications
* app

Each module encapsulates its domain logic and exposes functionality through well-defined interfaces.

## Consequences

Advantages:

* simpler deployment
* easier debugging
* lower operational complexity
* strong domain boundaries
* easier future extraction into microservices

Disadvantages:

* requires discipline to maintain module boundaries
* teams must avoid cross-module coupling

## Future Evolution

Modules may later be extracted into independent services:

```text
identity-service
catalog-service
inventory-service
order-service
payment-service
notification-service
```

The modular monolith approach ensures that this transition can be performed incrementally.

