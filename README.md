# 🗂️ Taskii

A full-stack Kanban-style project and task management application built with Angular, Spring Boot, and MySQL.

##  Technologies Used

### Frontend
- **Angular (TypeScript)** - Frontend framework
- **Angular Material** - UI components
- **RxJS** - Reactive programming
- **HTTP Interceptors** - Token handling

### Backend
- **Spring Boot** - Java backend framework
- **Spring MVC** - Web support
- **Spring Security** - Authentication & authorization
- **Spring Data JPA** - ORM with Hibernate
- **JWT (JSON Web Tokens)** - Stateless authentication
- **JUnit** - Unit testing
- **Lombok** - Reduces boilerplate code in Java classes
- **MySQL** - Relational database
- **Liquibase** - Database management
- **Docker** - Containerization

## Setup & Installation

### Prerequisites
- Java 17
- Node.js (currently using v22.17.1)
- Angular CLI (currently using v20.0.3)
- Maven 3 (currently using v3.8.5)
- - Docker

### Configuration
Add `.env` file in root folder with the following configuration:


```configuraton
MYSQL_PASSWORD= your-docker-db-password
MYSQL_HOST= your-docker-db-host
JWT_SECRET=your-jwt-key
```

Start backend:
```console
cd Task-Management-System
docker compose up --build
```

Start frontend:
```console
cd Task-Management-System/frontend/frontend
ng serve
```

##  Features


- User registration and login with secure authentication
- JWT-based authentication with Spring Security
- Full CRUD operations for projects (create, update, disable, delete)
- Complete task management (add, edit, delete tasks within projects)
- Drag-and-drop functionality for Kanban board task management
- Advanced search and filtering capabilities for tasks and projects
- Collaborator management: add/remove collaborators
- Add assignees to tasks
- Autherization based on collaborator roles
- Add/remove comments on tasks
- Responsive UI built with Angular Material
- Persistent data storage using MySQL database


##  Future Plans

- Implement keycloak auth
