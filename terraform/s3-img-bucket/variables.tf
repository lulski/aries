variable "terraform_user" {
  description = "IAM username of the Terraform service account"
  type        = string
  sensitive   = true
}
variable "terraform_user_arn" {
  description = "ARN of the Terraform service account"
  type        = string
  sensitive   = true
}
