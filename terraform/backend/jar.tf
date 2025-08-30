variable "region" {
  description = "AWS region to deploy resources"
  type        = string
  default     = "ap-southeast-2"
}

# terraform {
#   backend "s3" {
#     bucket = "aries-tf-state"
#     key    = "global/backend/terraform.tfstate"
#     region = "ap-southeast-2"

#     dynamodb_table = "aries-tf-state-locks"
#     encrypt        = true
#   }
# }

### JAR start

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

# Explicitly allow all public access to the S3 bucket
resource "aws_s3_bucket_public_access_block" "disable_block" {
  bucket                  = aws_s3_bucket.aries.id
  block_public_acls       = false
  block_public_policy     = false
  ignore_public_acls      = false
  restrict_public_buckets = false
}

# resource "aws_s3_bucket_policy" "public_read" {
#   bucket = aws_s3_bucket.aries.id

#   policy = jsonencode({
#     Version = "2012-10-17"
#     Statement = [
#       {
#         Effect    = "Allow"
#         Principal = "*"
#         Action    = "s3:GetObject"
#         Resource  = "${aws_s3_bucket.aries.arn}/*"
#       }
#     ]
#   })

# }



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

# output "aries_backend_jar_url" {
#   description = "Public URL of the aries-backend.jar in S3 (if bucket/object is public)"
#   value       = "https://${aws_s3_bucket.aries.bucket}.s3.${var.region}.amazonaws.com/${aws_s3_object.aries_backend_jar.key}"
# }
