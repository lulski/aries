# Terraform MongoDB Community Edition on AWS EC2

This configuration deploys MongoDB Community Edition on an Ubuntu EC2 instance.

## Usage

1. Set your AWS credentials and update `variables.tf` with your AMI ID and SSH key name.
2. Initialize Terraform:
   ```bash
   terraform init
   ```
3. Plan and apply:
   ```bash
   terraform apply
   ```
4. After deployment, connect via SSH and verify MongoDB is running.

**Note:** For production, restrict `mongodb_cidr` to trusted IPs
