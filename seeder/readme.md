# Aries Seeder

This module provides batch data seeding utilities for the Aries project. It uses Spring Batch to populate the database with initial or test data, such as users and posts.

## Features

- Batch processing with Spring Batch
- Seed users, roles, and posts into the database
- Customizable data sources and processors
- Logging and error handling

## Requirements

- Java 17+
- Gradle
- MongoDB (or the database used by Aries backend)

## Getting Started

1. **Install dependencies:**

   ```sh
   ./gradlew build
   ```

2. **Configure environment variables:**

   - Update `application.yml` or `.env` with your database connection details.

3. **Run the seeder:**

   ```sh
   ./gradlew bootRun
   ```

   The seeder will execute batch jobs to populate the database.

## Project Structure

- `src/main/java/com/aries/batch/db/seeder/` — Batch jobs, processors, and configuration
- `src/main/resources/` — Job configuration files and data templates

## Customization

- Modify or add seed data in the resources or processor classes.
- Adjust batch job configuration in `application.yml` or job config files.

## Useful Commands

- **Run tests:**

  ```sh
  ./gradlew test
  ```

- **Clean build:**
  ```sh
  ./gradlew clean build
  ```

## Contributing

1. Fork the repo and create your branch.
2. Make your changes and add tests.
3. Submit a pull request.

## License

MIT License.
