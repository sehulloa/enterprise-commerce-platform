variable "aws_region" {
  description = "AWS region where the infrastructure would be provisioned."
  type        = string
  default     = "us-east-1"
}

variable "project_name" {
  description = "Project name used for resource naming."
  type        = string
  default     = "enterprise-commerce-platform"
}

variable "environment" {
  description = "Deployment environment name."
  type        = string
  default     = "dev"
}
