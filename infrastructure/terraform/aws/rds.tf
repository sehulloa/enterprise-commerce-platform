resource "aws_db_subnet_group" "db" {
  name       = "${local.name_prefix}-db-subnet-group"
  subnet_ids = ["subnet-123", "subnet-456"] # placeholder

  tags = local.common_tags
}

resource "aws_db_instance" "postgres" {
  identifier = "${local.name_prefix}-postgres"

  engine         = "postgres"
  engine_version = "15"
  instance_class = "db.t3.micro"

  allocated_storage = 20

  db_name  = "enterprise_commerce"
  username = "ecp_admin"
  password = "change-me" # placeholder

  db_subnet_group_name = aws_db_subnet_group.db.name

  skip_final_snapshot = true

  publicly_accessible = true

  tags = local.common_tags
}
