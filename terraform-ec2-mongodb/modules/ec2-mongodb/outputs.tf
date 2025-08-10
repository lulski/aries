resource "aws_instance" "mongodb" {
  ami           = var.ami_id
  instance_type = var.instance_type
  security_groups = [aws_security_group.mongodb_sg.name]

  user_data = <<-EOF
              #!/bin/bash
              apt-get update
              apt-get install -y mongodb
              systemctl start mongodb
              systemctl enable mongodb
              EOF
}

resource "aws_security_group" "mongodb_sg" {
  name        = "mongodb_sg"
  description = "Allow MongoDB access"

  ingress {
    from_port   = 27017
    to_port     = 27017
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

output "instance_id" {
  value = aws_instance.mongodb.id
}

output "public_ip" {
  value = aws_instance.mongodb.public_ip
}