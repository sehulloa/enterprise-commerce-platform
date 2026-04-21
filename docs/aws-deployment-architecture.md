# AWS Deployment Architecture

## Objective

Define the production deployment architecture for Enterprise Commerce Platform on AWS.

This document establishes the target runtime environment for Phase 21 and clarifies which components are used in production versus local development.

---

## Production Architecture Overview

The application will be deployed on AWS using managed services where it makes operational sense.

### Core Components

- **Application Runtime:** Amazon ECS with AWS Fargate
- **Container Registry:** Amazon ECR
- **Database:** Amazon RDS for PostgreSQL
- **Message Broker:** Amazon MQ for RabbitMQ
- **Public Entry Point:** Application Load Balancer (ALB)
- **Application Logs:** Amazon CloudWatch Logs
- **Secrets Management:** AWS Secrets Manager or AWS Systems Manager Parameter Store

---

## Responsibilities by Service

### Amazon ECS with Fargate
Runs the Spring Boot application container in production without managing EC2 servers.

### Amazon ECR
Stores versioned Docker images published by the CI/CD pipeline.

### Amazon RDS for PostgreSQL
Provides the managed relational database used by the application in production.

### Amazon MQ for RabbitMQ
Provides the managed RabbitMQ broker used for asynchronous messaging between modules.

### Application Load Balancer
Routes external HTTP traffic to the ECS service.

### CloudWatch Logs
Stores runtime logs for troubleshooting and deployment validation.

### Secrets Manager / Parameter Store
Stores sensitive runtime configuration such as database credentials, broker credentials, JWT secret, and other production properties.

---

## Local vs Production Responsibility Split

### Local / Development
- docker-compose
- local PostgreSQL container
- local RabbitMQ container
- local SonarQube container
- app executed with local profile

### Production
- ECS/Fargate application container
- RDS PostgreSQL
- Amazon MQ RabbitMQ
- ALB
- CloudWatch
- externalized secrets and runtime configuration

---

## High-Level Request Flow

1. Client sends HTTP request
2. Application Load Balancer receives traffic
3. ALB forwards request to ECS service
4. Spring Boot app processes request
5. App persists data in RDS PostgreSQL
6. App publishes/consumes messages through Amazon MQ
7. Logs are sent to CloudWatch

---

## Required Runtime Configuration

The production application must receive configuration externally.

### Database
- DB host
- DB port
- DB name
- DB username
- DB password

### RabbitMQ
- RabbitMQ host
- RabbitMQ port
- RabbitMQ username
- RabbitMQ password

### Security
- JWT secret

### Spring runtime
- active profile
- server port
- logging level
- datasource configuration
- rabbitmq configuration

---

## Deployment Strategy

The CI/CD pipeline will:
1. build the application
2. run tests
3. run SonarQube analysis
4. build Docker image
5. publish image to Amazon ECR
6. deploy updated image to ECS service

---

## Non-Goals for This Phase

This phase does not create AWS infrastructure yet.
This phase only defines the target architecture and deployment direction.

Infrastructure creation, deployment automation, and validation will be implemented in later subphases.

---

## Related Documentation

- `docs/runtime-deployment.md`
- `docs/release-flow.md`
- `docs/current-project-status.md`
- `docs/project-roadmap.md`
