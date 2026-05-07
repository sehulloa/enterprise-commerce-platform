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
