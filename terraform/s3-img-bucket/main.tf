

# s3 aries-image-bucket

resource "aws_s3_bucket" "aries-img-bucket" {
  bucket = "aries-img-bucket"
  tags = {
    name = "aries-img-bucket"
  }

  lifecycle {
    prevent_destroy = false # ensures Terraform can destroy the bucket
  }
}

resource "aws_s3_bucket_public_access_block" "public_access_block" {
  bucket                  = aws_s3_bucket.aries-img-bucket.id
  block_public_acls       = false
  block_public_policy     = false
  ignore_public_acls      = false
  restrict_public_buckets = false
}

resource "aws_iam_policy" "aries-img-bucket-policy" {
  depends_on = [aws_s3_bucket_public_access_block.public_access_block]
  name       = "aries-img-bucket-policy"

  path        = "/"
  description = "aries-image-bucket policy allowing service account to make changes "

  policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Action = [
          "s3:PutObjectAcl",    # Crucial action for setting object ACLs
          "s3:GetObjectAcl",    # Allows getting the ACL of an object
          "s3:PutBucketPolicy", #Allows putting bucket policies
        ]
        Effect = "Allow"
        Resource = [
          "arn:aws:s3:::aries-img-bucket/*", # Applies to objects within the bucket
          "arn:aws:s3:::aries-img-bucket",   # Applies to the bucket itself
        ]
      },
    ]
  })
}

resource "aws_s3_bucket_policy" "public_read_policy" {
  bucket     = aws_s3_bucket.aries-img-bucket.id
  depends_on = [aws_s3_bucket_public_access_block.public_access_block]

  policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Sid       = "PublicReadGetObject"
        Effect    = "Allow"
        Principal = "*"
        Action    = "s3:GetObject"
        Resource  = "${aws_s3_bucket.aries-img-bucket.arn}/*"
      }
    ]
  })
}

resource "aws_iam_role" "aries_img_bucket_role" {
  name = "aries-img-bucket-role"

  # The assume_role_policy defines who/what can assume this role.
  # For example, if it's your EC2 instance, use the EC2 service principal.
  # If it's a Lambda function, change "ec2.amazonaws.com" to "lambda.amazonaws.com".
  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Effect = "Allow"
        Principal = {
          AWS = "arn:aws:iam::${var.terraform_user_arn}:user/${var.terraform_user}"
        }
        Action = "sts:AssumeRole"
      }
    ]
  })

  tags = {
    Name = "aries-img-bucket-role"
  }
}

resource "aws_iam_role_policy_attachment" "aries_img_bucket_role_attach" {
  role       = aws_iam_role.aries_img_bucket_role.name
  policy_arn = aws_iam_policy.aries-img-bucket-policy.arn
}

# resource "aws_iam_user_policy_attachment" "lulski_attach_aries_policy" {
#   user       = ${var.terraform_user}
#   policy_arn = aws_iam_policy.aries-img-bucket-policy.arn
# }


output "aries_img_bucket_arn" {
  value = aws_s3_bucket.aries-img-bucket.arn
}

output "bucket_url" {
  value = "https://${aws_s3_bucket.aries-img-bucket.bucket}.s3.amazonaws.com"
}
