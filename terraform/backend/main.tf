provider "aws" {
  region = "ap-southeast-2"
}

# resource "aws_instance" "web" {
#   ami                    = "ami-010876b9ddd38475e"
#   instance_type          = "t2.micro"
#   vpc_security_group_ids = [aws_security_group.instance.id]

#   user_data = <<-EOF
#               #!/bin/bash
#               echo "Hello, World" > index.html
#               nohup busybox httpd -f -p ${var.server_port} &
#               EOF

#   user_data_replace_on_change = true

#   tags = {
#     Name = "aries-backend"
#   }
# }

resource "aws_security_group" "instance" {
  name = "aries-backend-instance"

  ingress {
    from_port   = var.server_port
    to_port     = var.server_port
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

# setup EC2 instances that can be controlled by AWS auto scaling group
resource "aws_launch_template" "aries" {
  instance_type = "t2.micro"
  image_id      = "ami-010876b9ddd38475e"
  name_prefix   = "aries-backend"

  user_data = base64encode(<<-EOF
              #!/bin/bash
              echo "Hello, World" > index.html
              nohup busybox httpd -f -p ${var.server_port} &
              EOF

# user_data = base64encode(<<-EOF
#               #!/bin/bash
#               sudo yum update -y
#               sudo amazon-linux-extras install java-openjdk11 -y
#               # Download your Spring Boot JAR from S3 (replace with your bucket and jar name)
#               aws s3 cp s3://your-bucket-name/your-app.jar /home/ec2-user/app.jar
#               # Run the Spring Boot JAR
#               nohup java -jar /home/ec2-user/app.jar --server.port=${var.server_port} > /home/ec2-user/app.log 2>&1 &
#               EOF              
  )

  network_interfaces {
    security_groups             = [aws_security_group.instance.id]
    associate_public_ip_address = true
  }

  lifecycle {
    create_before_destroy = true
  }
}

data "aws_vpc" "default" {
  default = true
}

data "aws_subnets" "default" {
  filter {
    name   = "vpc-id"
    values = [data.aws_vpc.default.id]
  }
}

#setup auto-scaling group to manage those EC2 instances https://registry.terraform.io/providers/hashicorp/aws/latest/docs/resources/autoscaling_group
resource "aws_autoscaling_group" "aries" {
  launch_template {
    id      = aws_launch_template.aries.id
    version = "$Latest"
  }

  vpc_zone_identifier = data.aws_subnets.default.ids

  target_group_arns = [aws_lb_target_group.asg.arn]
  health_check_type = "ELB"

  min_size = 2
  max_size = 4

  tag {
    key                 = "Name"
    value               = "aries-backend"
    propagate_at_launch = true
  }
}

#setup AWS load balancer for those EC2 instances
resource "aws_lb" "aries" {
  name               = "aries-backend"
  load_balancer_type = "application"
  subnets            = data.aws_subnets.default.ids
  security_groups    = [aws_security_group.alb.id]
}

#setup AWS load balancer listener
resource "aws_lb_listener" "http" {
  port              = 80
  protocol          = "HTTP"
  load_balancer_arn = aws_lb.aries.arn

  default_action {
    type = "fixed-response"

    fixed_response {
      content_type = "text/plain"
      message_body = "404: page not found"
      status_code  = 404
    }
  }
}

#configure AWS load balancer to allow traffic from what is defined below
resource "aws_security_group" "alb" {
  name = "aries-alb"

  # Allow inbound HTTP requests
  ingress {
    from_port   = 80
    to_port     = 80
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  # Allow all outbound request
  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

#configure AWS load balancer to route to healthy instance
resource "aws_lb_target_group" "asg" {
  name     = "aries-asg"
  port     = var.server_port
  protocol = "HTTP"
  vpc_id   = data.aws_vpc.default.id

  health_check {
    path                = "/"
    protocol            = "HTTP"
    matcher             = "200"
    interval            = 15
    timeout             = 3
    healthy_threshold   = 2
    unhealthy_threshold = 2
  }
}

resource "aws_lb_listener_rule" "asg" {
  listener_arn = aws_lb_listener.http.arn
  priority     = 100

  condition {
    path_pattern {
      values = ["*"]
    }
  }

  action {
    type             = "forward"
    target_group_arn = aws_lb_target_group.asg.arn
  }
}

