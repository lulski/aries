provider "aws" {
  region = var.region
}

terraform {
  backend "s3" {
    bucket = "aries-tf-state"
    key    = "global/backend-single-ec2/terraform.tfstate"
    region = "ap-southeast-2"

    dynamodb_table = "aries-tf-state-locks"
    encrypt        = true
  }
}

resource "aws_key_pair" "aries" {
  key_name   = "aries-key"
  public_key = file("~/.ssh/aries-key.pub")
}

resource "aws_security_group" "instance" {
  name = "aries-backend-instance"

  ingress {
    from_port   = var.server_port_backend
    to_port     = var.server_port_backend
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  # Allow SSH
  ingress {
    from_port   = 22
    to_port     = 22
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

resource "aws_instance" "backend" {
  depends_on             = [aws_s3_bucket.aries]
  instance_type          = "t3.micro"
  ami                    = "ami-010876b9ddd38475e"
  vpc_security_group_ids = [aws_security_group.instance.id]
  key_name               = aws_key_pair.aries.key_name

  iam_instance_profile = aws_iam_instance_profile.ec2_profile.id

  user_data = base64encode(<<-EOF
                #!/bin/bash

                #install JRE
                sudo apt-get update
                sudo apt-get install -y openjdk-21-jre-headless
                sudo apt-get install -y unzip curl

                # Install AWS CLI v2
                curl "https://awscli.amazonaws.com/awscli-exe-linux-x86_64.zip" -o "awscliv2.zip"
                unzip awscliv2.zip
                ./aws/install

                #inject database connection string
                echo 'SPRING_DATA_MONGO_DB_URI="${var.SPRING_DATA_MONGO_DB_URI}"' | sudo tee -a /etc/environment

                echo -e "Aries Backend:\n" > index.html
                echo -e "Java version: $(java -version 2>&1 | head -n 1)" >> index.html

                #Download Aries-jar
                aws s3 cp s3://aries-springboot-jar/aries_jar /home/ubuntu/aries-backend.jar

                #run 
                nohup java -jar /home/ubuntu/aries-backend.jar --server.port=${var.server_port_backend} --spring.profiles.active=prod > /home/ubuntu/app.log 2>&1 &
                EOF        
  )

  user_data_replace_on_change = true

  tags = {
    Name = "aries-backend"
  }
}

############ JAR start

#https://registry.terraform.io/providers/hashicorp/aws/latest/docs/resources/s3_bucket 
resource "aws_s3_bucket" "aries" {
  bucket = "aries-springboot-jar"
}

resource "aws_s3_object" "aries_backend_jar" {
  bucket = aws_s3_bucket.aries.id
  key    = "aries_jar"
  source = "../../backend/build/libs/aries-backend.jar"
}

# versioning the s3 bucket
#https://registry.terraform.io/providers/hashicorp/aws/latest/docs/resources/s3_bucket_versioning
resource "aws_s3_bucket_versioning" "aries_versioning" {
  bucket = aws_s3_bucket.aries.id
  versioning_configuration {
    status = "Enabled"
  }
}

# encrypt all data written into the bucket, might be useful as it might store passwords etc
resource "aws_s3_bucket_server_side_encryption_configuration" "default" {
  bucket = aws_s3_bucket.aries.id

  rule {
    apply_server_side_encryption_by_default {
      sse_algorithm = "AES256"
    }
  }
}

# IAM roles for EC2 to access jar
resource "aws_iam_role" "ec2_s3_read" {
  name = "ec2-s3-read-role"
  assume_role_policy = jsonencode({
    Version = "2012-10-17",
    Statement = [{
      Effect = "Allow",
      Principal = {
        Service = "ec2.amazonaws.com"
      },
      Action = "sts:AssumeRole"
    }]
  })
}

resource "aws_iam_policy" "s3_read_access" {
  name        = "ec2-s3-read-access"
  description = "Allow EC2 to read objects from aries-springboot-jar"
  policy = jsonencode({
    Version = "2012-10-17",
    Statement = [{
      Effect   = "Allow",
      Action   = ["s3:GetObject"],
      Resource = "${aws_s3_bucket.aries.arn}/*"
    }]
  })
}

resource "aws_iam_role_policy_attachment" "attach_s3_read" {
  role       = aws_iam_role.ec2_s3_read.name
  policy_arn = aws_iam_policy.s3_read_access.arn
}

resource "aws_iam_instance_profile" "ec2_profile" {
  name = "ec2-s3-read-profile"
  role = aws_iam_role.ec2_s3_read.name
}

output "bucket_arn" {
  value = aws_s3_bucket.aries.arn
}
