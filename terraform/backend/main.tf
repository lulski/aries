provider "aws" {
  region = "ap-southeast-2"
}

resource "aws_instance" "web" {
  ami                    = "ami-010876b9ddd38475e"
  instance_type          = "t2.micro"
  vpc_security_group_ids = [aws_security_group.instance.id]

  user_data = <<-EOF
              #!/bin/bash
              echo "Hello, World" > index.html
              nohup busybox httpd -f -p ${var.server_port} &
              EOF

  user_data_replace_on_change = true

  tags = {
    Name = "aries-backend"
  }
}

resource "aws_security_group" "instance" {
  name = "aries-backend-instance"

  ingress {
    from_port   = var.server_port
    to_port     = var.server_port
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

# resource "aws_s3_bucket" "backend_bucket" {
#   bucket = "my-unique-backend-bucket"
#   acl    = "private"
# }

# resource "aws_dynamodb_table" "terraform_lock_table" {
#   name         = "terraform-lock-table"
#   billing_mode = "PAY_PER_REQUEST"
#   attribute {
#     name = "LockID"
#     type = "S"
#   }
#   hash_key = "LockID"
# }

# resource "aws_iam_role" "terraform_role" {
#   name = "terraform-backend-role"

#   assume_role_policy = jsonencode({
#     Version = "2012-10-17"
#     Statement = [
#       {
#         Action    = "sts:AssumeRole"
#         Principal = {
#           Service = "ec2.amazonaws.com"
#         }
#         Effect    = "Allow"
#         Sid       = ""
#       },
#     ]
#   })
# }

# resource "aws_iam_role_policy" "terraform_policy" {
#   name   = "terraform-backend-policy"
#   role   = aws_iam_role.terraform_role.id
#   policy = jsonencode({
#     Version = "2012-10-17"
#     Statement = [
#       {
#         Action   = [
#           "s3:*",
#           "dynamodb:*"
#         ]
#         Effect   = "Allow"
#         Resource = "*"
#       },
#     ]
#   })
# }
