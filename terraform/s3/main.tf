
variable "region" {
  description = "AWS region to deploy resources"
  type        = string
  default     = "ap-southeast-2"
}

provider "aws" {
  region = var.region
}

# provision s3bucket called aries-tf-state
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

# configure this terraform context to put the state in the above s3 bucket
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
