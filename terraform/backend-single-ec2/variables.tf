variable "server_port_backend" {
  description = "Spring boot app port"
  type        = number
  default     = 8181
}

variable "SPRING_DATA_MONGO_DB_URI" {
  description = "MongoDB Atlas database connection string"
  type        = string
  sensitive   = true
}


variable "region" {
  description = "AWS region"
  type        = string
  default     = "ap-southeast-2"
}
