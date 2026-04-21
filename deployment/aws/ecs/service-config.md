# ECS Service Base Configuration

## Service Name
enterprise-commerce-platform-service

## Launch Type
FARGATE

## Desired Tasks
1

## Container
- name: app
- port: 8080

## Load Balancer
- type: Application Load Balancer
- target type: ip
- health check path: /actuator/health
- health check port: traffic-port

## Networking
- ECS tasks will use awsvpc networking
- Tasks will run in private subnets
- Load balancer will run in public subnets
- Security groups must allow:
  - ALB -> ECS app port 8080
  - ECS -> RDS PostgreSQL port 5432
  - ECS -> Amazon MQ RabbitMQ port 5672

## Logging
- CloudWatch log group: /ecs/enterprise-commerce-platform

## Deployment Notes
- The task definition image must be replaced by the image published to Amazon ECR
- Secrets must be injected through AWS Secrets Manager
- Health check path must remain aligned with Spring Boot actuator exposure
