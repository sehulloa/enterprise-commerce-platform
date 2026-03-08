# ADR-002 — Use Flyway for Database Schema Management

## Status

Accepted

## Context

Database schema changes must be managed in a controlled and versioned manner.

Possible approaches considered:

1. Manual SQL scripts
2. Hibernate auto schema generation
3. Migration tool (Flyway or Liquibase)

Using Hibernate auto schema generation (`ddl-auto`) is convenient during development but is not suitable for production environments because:

* schema changes are not versioned
* changes are not auditable
* rollback strategies are difficult

Manual scripts introduce coordination problems between developers.

## Decision

The system will use **Flyway** to manage database schema migrations.

All database changes will be versioned using SQL migration scripts.

Migration scripts will follow the Flyway naming convention:

```text
V1__initial_schema.sql
V2__identity_access_tables.sql
V3__seed_identity_data.sql
```

Migration scripts will be stored in:

```text
app/src/main/resources/db/migration
```

## Consequences

Advantages:

* version-controlled database schema
* reproducible environments
* safe database evolution
* integration with CI/CD pipelines

Disadvantages:

* requires discipline in managing migrations
* developers must coordinate schema changes

## Additional Notes

Hibernate schema generation will be configured as:

```text
ddl-auto: validate
```

This ensures that Hibernate validates the schema but does not modify it automatically.
