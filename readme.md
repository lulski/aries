# Project Aries

Aries is a full-stack CRUD web application featuring a reactive backend and a modern frontend.

## Tech Stack

- **Backend**
  - Spring Boot
  - WebFlux (Reactive)
  - Spring Security
  - MongoDB integration
- **Frontend**
  - Next.js (React)
  - Mantine UI
- **Database**
  - MongoDB

---

## Prerequisites

- Java 17+ (JRE 21 recommended)
- MongoDB running on `localhost:27017`
- Node.js & npm

## Getting Started

### Run the database

```
podman volume create aries_mongodb_data && podman volume create aries_mongodb_config

podman run -d \
  --name mongodb \
  -p 27017:27017 \
  -v aries_mongodb_data:/data/db:Z \
  -v aries_mongodb_config:/data/configdb:Z \
  library/mongo:8.0.4
```

### Run the Backend (Development)

```sh
cd ./aries/backend
./gradlew bootRun
```

### Run the Frontend

```sh
cd ./aries/frontend
npm install
npm run dev
```

> **Note:** If your frontend is in a subfolder (e.g., `frontend/ram`), adjust the path accordingly.

### Run the Seeder (Optional: Populate Database)

```sh
cd ./aries/seeder
./gradlew bootRun
```

## Project Structure

- `backend/` — Spring Boot WebFlux backend API
- `frontend/` — Next.js frontend app
- `seeder/` — Spring Batch database seeder

---

## License

MIT License. See [LICENSE](./LICENSE) for details.
