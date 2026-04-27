# Deployment Validation

## Objective

Define the minimum validation flow required after deploying Enterprise Commerce Platform to a production-like environment.

This checklist is intended to verify that the application is reachable, healthy, and correctly connected to its required runtime dependencies.

---

## Post-Deployment Validation Checklist

### 1. Application Availability
- Verify that the application endpoint is reachable through the load balancer
- Confirm that the application responds without connection errors

### 2. Health Endpoint
- Verify `GET /actuator/health`
- Expected result: HTTP 200
- Expected response contains application health status

### 3. Application Logs
- Verify that container logs are being sent to the centralized logging destination
- Confirm that the application starts successfully without fatal exceptions
- Confirm there are no repeated restart loops

### 4. Database Connectivity
- Verify that the application starts with successful datasource initialization
- Confirm there are no PostgreSQL connection errors
- Confirm Flyway migration check completes successfully

### 5. RabbitMQ Connectivity
- Verify that the application connects successfully to RabbitMQ
- Confirm there are no authentication or host resolution errors
- Confirm listeners start correctly when applicable

### 6. Security and Runtime Configuration
- Verify that production runtime variables are being resolved correctly
- Confirm the application is running with `prod` profile
- Confirm Swagger is disabled in production profile

### 7. Functional Smoke Test
- Execute at least one basic API request
- Confirm the API returns a valid application response
- Confirm no unexpected 500 errors occur in the initial smoke flow

---

## Validation Result

A deployment should be considered valid only if:

- health endpoint is UP
- logs are available
- database connectivity is successful
- RabbitMQ connectivity is successful
- the application responds to at least one smoke test request

---

## Related Documentation

- `docs/runtime-deployment.md`
- `docs/aws-deployment-architecture.md`
