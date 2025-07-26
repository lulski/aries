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

## Backend To-do

- [ ] Add more API endpoints
- [ ] Improve authentication/authorization
- [ ] Add integration tests

## Frontend To-do

- [ ] Implement user registration
- [ ] Add post editing/deletion
- [ ] Improve UI/UX

---

## License

MIT License. See [LICENSE](./LICENSE) for details.
