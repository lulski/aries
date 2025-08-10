# Terraform EC2 MongoDB Module

This module provisions an EC2 instance with MongoDB Community Edition installed. It is designed to be reusable and configurable for different environments.

## Usage

To use this module, include it in your Terraform configuration as follows:

```hcl
module "ec2_mongodb" {
  source          = "../modules/ec2-mongodb"
  instance_type   = var.instance_type
  ami_id          = var.ami_id
  security_groups = var.security_groups
  key_name        = var.key_name
}
```

## Input Variables

| Name              | Description                                   | Type   | Default |
|-------------------|-----------------------------------------------|--------|---------|
| instance_type     | The type of EC2 instance to create            | string | `t2.micro` |
| ami_id            | The AMI ID to use for the instance            | string | `ami-0c55b159cbfafe1f0` |
| security_groups   | List of security group IDs to associate       | list   | `[]` |
| key_name          | The name of the key pair to use for SSH access| string | `null` |

## Outputs

| Name              | Description                                   |
|-------------------|-----------------------------------------------|
| public_ip         | The public IP address of the EC2 instance     |

## Requirements

- Terraform 0.12 or later
- AWS account with appropriate permissions to create EC2 instances

## Example

Refer to the `environments/dev` directory for an example of how to use this module in a specific environment.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.