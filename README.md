# 🗂️ Taskii: Project Management app

A full-stack Kanban-style project and task management application built with Angular, Spring Boot, and MySQL.

## 🛠️ Technologies Used

### Frontend
- **Angular (TypeScript)** - Frontend framework
- **Angular Material** - UI components
- **RxJS** - Reactive programming
- **HTTP Interceptors** - Token handling

### Backend
- **Spring Boot** - Java backend framework
- **Spring Security** - Authentication & authorization
- **Spring Data JPA** - ORM with Hibernate
- **JWT (JSON Web Tokens)** - Stateless authentication
- **MySQL** - Relational database
- **Lombok** - Reduces boilerplate code in Java classes

## 🧰 Setup & Installation

### Prerequisites
- Java 17
- Node.js + Angular CLI (currently using 13.3.7, planning on update)
- MySQL server
- Maven

### Configuration
Add `application.properties` in resources with preferred port, JWT token and MySQL server config:

```properties
spring.datasource.url=jdbc:mysql://localhost:port/schema?useSSL=false&allowPublicKeyRetrieval=true
spring.datasource.username=your-username
spring.datasource.password=your-password
server.port=8080

jwt.secret=your-jwt-key
```

### Database Init (Current Setup)

```sql
CREATE TABLE users (
    user_id VARCHAR(36) PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    enabled BOOLEAN NOT NULL
);

CREATE TABLE projects (
    project_id VARCHAR(36) PRIMARY KEY,
    project_name VARCHAR(100) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    active BOOLEAN NOT NULL,
    parent_id INT,
    user_id VARCHAR(36) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

CREATE TABLE tasks (
    task_id VARCHAR(36) PRIMARY KEY,
    task_title VARCHAR(100) NOT NULL,
    task_status VARCHAR(50) NOT NULL,
    task_priority INT NOT NULL,
    task_date TIMESTAMP NOT NULL,
    task_desc TEXT,
    project_id VARCHAR(36) NOT NULL,
    FOREIGN KEY (project_id) REFERENCES projects(project_id)
);

CREATE TABLE collaborators (
    collaborator_id VARCHAR(36) PRIMARY KEY,
    user_id VARCHAR(36) NOT NULL,
    project_id VARCHAR(36) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (project_id) REFERENCES projects(project_id)
);

CREATE TABLE statuses (
    status_id VARCHAR(36) PRIMARY KEY,
    status_name VARCHAR(36),
    order_number INT UNSIGNED,
    project_id VARCHAR(36) NOT NULL
);

```
## 🚀 Features

- ✅ User registration and login with secure authentication
- 🛡️ JWT-based authentication with Spring Security
- 📁 Full CRUD operations for projects (create, update, disable, delete)
- 📝 Complete task management (add, edit, delete tasks within projects)
- 🎯 Drag-and-drop functionality for Kanban board task management
- 🔍 Advanced search and filtering capabilities for tasks
- 🧩 Responsive UI built with Angular Material
- 🗄️ Persistent data storage using MySQL database

## 🧭 Future Plans

- 👥 Collaboration features (invite users to work on projects)
- 🛡️ Role-based permissions within projects (e.g., Owner, Contributor, Viewer)
- ⚙️ Upgrade Angular to the latest stable version for performance and security improvements
