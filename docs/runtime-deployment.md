# Runtime & Deployment Guide

## Overview

This project supports multiple runtime profiles:

- dev → local development
- docker → containerized runtime
- prod → production-like setup

---

## Runtime Profiles

### dev
- local PostgreSQL
- local RabbitMQ
- Swagger enabled
- Actuator enabled

### docker
- services via Docker Compose
- Swagger enabled
- Actuator enabled

### prod
- requires environment variables
- Swagger disabled
- restricted actuator

---

## Run Locally (without Docker)

Run the application:

    gradlew.bat :app:bootRun --args="--spring.profiles.active=dev"

Verify:

- http://localhost:8080
- http://localhost:8080/swagger-ui.html
- http://localhost:8080/actuator/health

---

## Run with Docker Compose

Start:

    docker compose up --build

Verify:

- http://localhost:8080
- http://localhost:8080/swagger-ui.html
- http://localhost:8080/actuator/health
- http://localhost:15672

Stop:

    docker compose down

Reset:

    docker compose down -v

---

## Ports

- 8080 → app
- 5432 → postgres
- 5672 → rabbitmq
- 15672 → rabbitmq UI

---

## Verification Checklist

- app starts
- Flyway runs
- DB connection OK
- RabbitMQ connection OK
- health endpoint returns UP
- Swagger works in dev/docker
- Swagger disabled in prod

---

## Troubleshooting

Validate compose:

    docker compose config

Reset DB:

    docker compose down -v
    docker compose up --build

Common issues:

- port already in use
- wrong DB credentials
- container networking (use service names: postgres, rabbitmq)

---

## Suggested Release Flow

- main → production
- develop → integration
- feature/* → development
- release/* → release

Flow:

1. feature → develop
2. create release branch
3. validate
4. merge to main
5. tag
6. merge back to develop

---

## Current Runtime Status

- environment-based configuration
- Dockerized runtime
- full stack working
- production-like setup  