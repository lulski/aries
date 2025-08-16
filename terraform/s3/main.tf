
variable "region" {
  description = "AWS region to deploy resources"
  type        = string
  default     = "ap-southeast-2"
}

provider "aws" {
  region = var.region
}

# this is to put terraform state in the cloud 
resource "aws_s3_bucket" "aries-tf-state" {
  bucket = "aries-tf-state"

  lifecycle {
    prevent_destroy = true
  }
}

# versioning so can see full revision history of state file
resource "aws_s3_bucket_versioning" "enabled" {
  bucket = aws_s3_bucket.aries-tf-state.id
  versioning_configuration {
    status = "Enabled"
  }
}

# encrypt the state data
resource "aws_s3_bucket_server_side_encryption_configuration" "encrypt-tf-state" {
  bucket = aws_s3_bucket.aries-tf-state.id

  rule {
    apply_server_side_encryption_by_default {
      sse_algorithm = "AES256"
    }
  }
}

resource "aws_s3_bucket_public_access_block" "public_access_block" {
  bucket                  = aws_s3_bucket.aries-tf-state.id
  block_public_acls       = true
  block_public_policy     = true
  ignore_public_acls      = true
  restrict_public_buckets = true
}

resource "aws_dynamodb_table" "terraform_locks" {
  name         = "aries-tf-state-locks"
  billing_mode = "PAY_PER_REQUEST"
  hash_key     = "LockID"

  attribute {
    name = "LockID"
    type = "S"
  }
}

terraform {
  backend "s3" {
    bucket = "aries-tf-state"
    key    = "global/s3/terraform.tfstate"
    region = "ap-southeast-2"

    dynamodb_table = "aries-tf-state-locks"
    encrypt        = true
  }
}

output "s3_bucket_arn" {
  value       = aws_s3_bucket.aries-tf-state.arn
  description = "The ARN of the S3 bucket"
}
output "dynamodb_table_name" {
  value       = aws_dynamodb_table.terraform_locks.name
  description = "The name of the DynamoDB table"
}

#### JAR start

# #https://registry.terraform.io/providers/hashicorp/aws/latest/docs/resources/s3_bucket 
# resource "aws_s3_bucket" "aries" {
#   bucket = "aries-springboot-jar"
# }

# resource "aws_s3_object" "aries_backend_jar" {
#   bucket = aws_s3_bucket.aries.id
#   key    = "aries_jar"
#   source = "../../backend/build/libs/aries-backend.jar"
# }

# # versioning the s3 bucket
# #https://registry.terraform.io/providers/hashicorp/aws/latest/docs/resources/s3_bucket_versioning
# resource "aws_s3_bucket_versioning" "aries_versioning" {
#   bucket = aws_s3_bucket.aries.id
#   versioning_configuration {
#     status = "Enabled"
#   }
# }

# # encrypt all data written into the bucket, might be useful as it might store passwords etc
# resource "aws_s3_bucket_server_side_encryption_configuration" "default" {
#   bucket = aws_s3_bucket.aries.id

#   rule {
#     apply_server_side_encryption_by_default {
#       sse_algorithm = "AES256"
#     }
#   }
# }

# # Explicitly allow all public access to the S3 bucket
# resource "aws_s3_bucket_public_access_block" "disable_block" {
#   bucket                  = aws_s3_bucket.aries.id
#   block_public_acls       = false
#   block_public_policy     = false
#   ignore_public_acls      = false
#   restrict_public_buckets = false
# }

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


# output "aries_backend_jar_url" {
#   description = "Public URL of the aries-backend.jar in S3 (if bucket/object is public)"
#   value       = "https://${aws_s3_bucket.aries.bucket}.s3.${var.region}.amazonaws.com/${aws_s3_object.aries_backend_jar.key}"
# }
