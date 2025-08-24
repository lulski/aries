variable "server_port" {
  description = "The port the server will use for HTTP requests"
  type        = number
  default     = 8080
}

variable "SPRING_DATA_MONGO_DB_URI" {
  description = "MongoDB Atlas database connection string"
  type        = string
  sensitive   = true
}
