variable "server_port" {
  description = "The port the server will use for HTTP requests"
  type        = number
  default     = 8181
}

variable "SPRING_DATA_MONGODB_URI" {
  description = "MongoDB Atlas database connection string"
  type        = string
  sensitive   = true
}


variable "region" {
  description = "AWS region"
  type        = string
  default     = "ap-southeast-2"
}
