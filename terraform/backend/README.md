# Terraform Backend Infrastructure

This directory contains Terraform configuration files for deploying the backend infrastructure.

## Structure

- `main.tf` - Main Terraform configuration for backend resources.
- `variables.tf` - Input variables for customizing the deployment.
- `outputs.tf` - Outputs from the Terraform deployment.

## Usage

1. **Initialize Terraform:**

   ```bash
   terraform init
   ```

2. **Review the execution plan:**

   ```bash
   terraform plan
   ```

3. **Apply the configuration:**
   ```bash
   terraform apply
   ```

## Requirements

- [Terraform](https://www.terraform.io/downloads.html) installed
- AWS credentials configured (if deploying to AWS)
- Update variable values as needed in `variables.tf` or via CLI

## todo

- deploy backend.jar to this EC2 instance
