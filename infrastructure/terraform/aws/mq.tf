resource "aws_mq_broker" "rabbitmq" {
  broker_name = "${local.name_prefix}-rabbitmq"

  engine_type        = "RabbitMQ"
  engine_version     = "3.11.20"
  host_instance_type = "mq.t3.micro"

  publicly_accessible = true

  user {
    username = "ecp_user"
    password = "esta-es-una-clave-secreta-muy-larga-y-segura-de-al-menos-32-caracteres" # placeholder
  }

  tags = local.common_tags
}
