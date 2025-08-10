variable "instance_type" {
  description = "The type of EC2 instance to use"
  default     = "t2.micro"
}

variable "ami_id" {
  description = "The AMI ID for the EC2 instance"
  default     = "ami-0c55b159cbfafe1f0" # Example AMI ID for Amazon Linux 2
}

variable "region" {
  description = "The AWS region to deploy the EC2 instance"
  default     = "us-east-1"
}

variable "key_name" {
  description = "The name of the key pair to use for SSH access"
  type        = string
}

variable "security_group_ids" {
  description = "The security group IDs to associate with the EC2 instance"
  type        = list(string)
}