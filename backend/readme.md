# Aries Backend

This is the backend service for the Aries project, built with Spring Boot and Project Reactor. It provides RESTful and reactive APIs for authentication, user management, and post handling.

## Features

- Reactive REST API using Spring WebFlux
- User authentication and authorization
- Post creation, listing, and management
- MongoDB integration
- Batch processing support

## Requirements

- Java 17+
- Gradle
- MongoDB

## Getting Started

1. **Clone the repository:**
   ```sh
   git clone https://github.com/your-org/aries.git
   cd aries/backend
   ```

2. **Configure environment variables:**
   - Copy `.env.example` to `.env` and update values as needed.

3. **Run MongoDB:**  
   Make sure MongoDB is running locally or update the connection string in `application.yml`.

4. **Build and run the backend:**
   ```sh
   ./gradlew bootRun
   ```

5. **API Documentation:**  
   Visit `http://localhost:8080/swagger-ui.html` (if Swagger is enabled).

## Project Structure

- `src/main/java/com/lulski/aries/`  
  Main source code for controllers, services, repositories, and configuration.
- `src/test/java/com/lulski/aries/`  
  Unit and integration tests.

## Useful Commands

- **Run tests:**  
  ```sh
  ./gradlew test
  ```
- **Build JAR:**  
  ```sh
  ./gradlew build
  ```

## Contributing

1. Fork the repo and create your branch.
2. Make your changes and add tests.
3. Submit a pull request.

## License

MIT License.
