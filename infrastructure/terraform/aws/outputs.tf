output "project_name" {
  description = "Project name used by Terraform."
  value       = var.project_name
}

output "environment" {
  description = "Environment name used by Terraform."
  value       = var.environment
}

output "aws_region" {
  description = "AWS region selected for this configuration."
  value       = var.aws_region
}

output "name_prefix" {
  description = "Common naming prefix for AWS resources."
  value       = local.name_prefix
}

output "ecr_repository_url" {
  description = "ECR repository URL for the application image."
  value       = aws_ecr_repository.app.repository_url
}

output "ecs_cluster_name" {
  value = aws_ecs_cluster.main.name
}

output "ecs_task_definition_family" {
  value = aws_ecs_task_definition.app.family
}

output "rds_endpoint" {
  value = aws_db_instance.postgres.endpoint
}

output "mq_broker_id" {
  value = aws_mq_broker.rabbitmq.id
}
