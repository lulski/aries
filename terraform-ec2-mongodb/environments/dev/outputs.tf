resource "aws_instance" "mongodb_instance" {
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

  tags = {
    Name = "MongoDB-Instance"
  }
}

output "instance_id" {
  value = aws_instance.mongodb_instance.id
}

output "public_ip" {
  value = aws_instance.mongodb_instance.public_ip
}