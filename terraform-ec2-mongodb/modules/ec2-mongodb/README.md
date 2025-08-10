# EC2 MongoDB Module

This module provisions an Amazon EC2 instance with MongoDB Community Edition installed. It is designed to be reusable and configurable for different environments.

## Usage

To use this module, include it in your Terraform configuration as follows:

```hcl
module "ec2_mongodb" {
  source     = "./modules/ec2-mongodb"
  instance_type = var.instance_type
  ami_id = var.ami_id
  key_name = var.key_name
  security_group_ids = var.security_group_ids
}
```

## Inputs

| Name                   | Description                                   | Type   | Default | Required |
|------------------------|-----------------------------------------------|--------|---------|:--------:|
| instance_type          | The type of instance to create                | string | `t2.micro` | no     |
| ami_id                 | The AMI ID to use for the instance            | string | n/a     | yes     |
| key_name               | The name of the key pair to use               | string | n/a     | yes     |
| security_group_ids     | A list of security group IDs to associate     | list   | n/a     | yes     |

## Outputs

| Name                   | Description                                   |
|------------------------|-----------------------------------------------|
| public_ip              | The public IP address of the EC2 instance     |
| instance_id            | The ID of the EC2 instance                     |

## Example

Here is an example of how to call this module in your Terraform configuration:

```hcl
module "mongodb_instance" {
  source              = "./modules/ec2-mongodb"
  instance_type      = "t2.micro"
  ami_id             = "ami-0c55b159cbfafe01e" # Example AMI ID
  key_name           = "my-key-pair"
  security_group_ids = ["sg-0123456789abcdef0"]
}
```

## Requirements

- Terraform 0.12 or later
- AWS Provider

## Author

Your Name

## License

This project is licensed under the MIT License.