# 🗂️ Taskii: Project Management app

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

## Setup & Installation

### Prerequisites
- Java 17
- Node.js + Angular CLI (currently using 20.0.3)
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

ALTER TABLE users ADD COLUMN email VARCHAR(128);
ALTER TABLE `users` ADD UNIQUE `tasks_uq_email`(`email`);

CREATE TABLE projects (
    project_id VARCHAR(36) PRIMARY KEY,
    project_name VARCHAR(100) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    active BOOLEAN NOT NULL,
    parent_id INT,
    user_id VARCHAR(36) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

ALTER TABLE `projects` ADD UNIQUE `projects_uq_01`(`project_name`, `user_id`);
ALTER TABLE projects ADD COLUMN project_desc text;

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

ALTER TABLE tasks MODIFY COLUMN task_date timestamp null;
ALTER TABLE tasks MODIFY COLUMN task_desc TEXT null;
ALTER TABLE tasks ADD COLUMN assignee VARCHAR(36);
ALTER TABLE tasks ADD COLUMN order_number INT UNSIGNED;

CREATE TABLE collaborators (
    collaborator_id VARCHAR(36) PRIMARY KEY,
    user_id VARCHAR(36) NOT NULL,
    project_id VARCHAR(36) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (project_id) REFERENCES projects(project_id)
);

ALTER TABLE collaborators ADD COLUMN role VARCHAR(36);

CREATE TABLE statuses (
    status_id VARCHAR(36) PRIMARY KEY,
    status_name VARCHAR(36),
    order_number INT UNSIGNED,
    project_id VARCHAR(36) NOT NULL
);

ALTER TABLE `statuses` ADD UNIQUE `statuses_uq_01`(`status_name`, `project_id`);

CREATE TABLE comments (
    comment_id VARCHAR(36) PRIMARY KEY,
    task_id VARCHAR(100) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    comment text NOT NULL,
    user_id VARCHAR(36) NOT NULL,
    FOREIGN KEY (task_id) REFERENCES tasks(task_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

```
##  Features

-  User registration and login with secure authentication
-  JWT-based authentication with Spring Security
-  Full CRUD operations for projects (create, update, disable, delete)
-  Complete task management (add, edit, delete tasks within projects)
-  Drag-and-drop functionality for Kanban board task management
-  Advanced search and filtering capabilities for tasks and projects
-  Collaborator management: add/remove collaborators
-  Add assignees to tasks
-  Autherization based on collaborator roles
-  Add/remove comments on tasks
-  Responsive UI built with Angular Material
-  Persistent data storage using MySQL database

##  Future Plans

-  Implement keycloak auth
