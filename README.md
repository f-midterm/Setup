# Project Title

A brief description of your project and what it does.

## Prerequisites

Once the services are running, you can access them at the following URLs:
- [Main Application (Frontend):](http://localhost)
- [Backend/API/Database Admin:](http://localhost:8081)

## Getting Started

Follow these steps to get your development environment set up and running.

### 1. Clone the Repository

```bash
git clone https://github.com/f-midterm/q.git
cd q
```

### 2. Start the Services
```bash
docker-compose -f docker-compose.yml -f docker-compose.dev.yml up --build -d
```

### 3. Stop the Services
```bash
docker-compose down
```

### 4. Databases Username - [Database Admin:](http://localhost:8081)
```bash
apartment
```

### 5. Databases Password - [Database Admin:](http://localhost:8081)
```bash
apartment_password
```
