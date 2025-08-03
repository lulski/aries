output "mongodb_public_ip" {
  description = "Public IP of the MongoDB EC2 instance"
  value       = aws_instance.mongodb.public_ip
}

output "mongodb_instance_id" {
  description = "Instance ID of the MongoDB EC2 instance"
  value       = aws_instance.mongodb.id
}