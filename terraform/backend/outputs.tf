output "alb_dns_name" {
  value       = aws_lb.aries.dns_name
  description = "The domain name of the load balancer"
}

output "aries_backend_instance_public_ips" {
  description = "Public IPs of Aries backend EC2 instances for MongoDB Atlas whitelist"
  value       = data.aws_instances.aries_asg.public_ips
}
