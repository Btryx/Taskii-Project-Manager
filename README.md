# 🗂️ Taskii

A full-stack Kanban-style project and task management application built with Angular, Spring Boot, and MySQL.

<div align="center">
  <img width="1000" alt="Taskii Dashboard Overview" src="https://github.com/user-attachments/assets/0e798249-58b2-4621-82a3-612c4be2382e" />
</div>

## Features

- User registration and login with secure authentication
- JWT-based authentication with Spring Security
- Manage your projects
- Manage project tasks
- Drag-and-drop kanban board functionality, easy to use
- Advanced search and filtering for tasks and projects
- Add, remove or rename columns dynamically
- Collaborator management: easily add or remove members as an admin
- Assign people to different tasks
- Autherization based on member roles
- Comment on tasks, see what others wrote
- Responsive UI
- Persistent data storage using MySQL
- Lightweight and fast

## Future Plans

- Implement Keycloak authentication
- HTTPS
- Change column orders
- Limit Viewer role permissions
- Taskii home page


## Technologies Used

- **Angular (TypeScript)** - Frontend framework
- **Angular Material** - UI components
- **RxJS** - Reactive programming
- **HTTP Interceptors** - Token handling

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
- Docker

### Configuration
Add `.env` file in root folder with the following configuration:

```env
MYSQL_PASSWORD=your-docker-db-password
MYSQL_HOST=your-docker-db-host
JWT_SECRET=your-jwt-key
```

### Run

**Start backend:**
```bash
cd Task-Management-System
docker compose up --build
```

**Start frontend:**
```bash
cd Task-Management-System/frontend/frontend
ng serve
```

Open browser at : [http://localhost:4200](http://localhost:4200)



## Screenshots

<table>
  <tr>
    <td align="center">
      <img width="500" alt="kép" src="https://github.com/user-attachments/assets/465618c7-bc34-4f35-a8a8-fb276d48bfb5" />
      <br><em>User Authentication</em>
    </td>
  </tr>
  <tr>
    <td align="center">
      <img width="900" alt="kép" src="https://github.com/user-attachments/assets/48275336-7099-47db-823c-bd798a2bcd7e" />
      <br><em>Project Management</em>
    </td>
  </tr>
  <tr>
    <td align="center">
      <img width="600" alt="kép" src="https://github.com/user-attachments/assets/a2d63a6d-bd27-4ced-8a5a-4d8fc6216e1b" />
      <br><em>Task Creation</em>
    </td>
  </tr>
  <tr>
    <td align="center">
      <img width="700" alt="kép" src="https://github.com/user-attachments/assets/1e066a57-00c9-487d-92a7-126938ec7896" />
      <br><em>Kanban Board</em>
    </td>
  </tr>
  <tr>
    <td align="center">
      <img width="600" alt="kép" src="https://github.com/user-attachments/assets/e3a713ba-707c-4a4b-9587-84663a3bc438" />
      <br><em>Search and Filter</em>
    </td>
  </tr>
  <tr>
    <td align="center">
      <img width="370" alt="kép" src="https://github.com/user-attachments/assets/cbe6fc30-da7e-4007-afaa-50738135fa48" />
      <br><em>Add members</em>
    </td>
  </tr>
  <tr>
    <td align="center">
      <img width="400" alt="kép" src="https://github.com/user-attachments/assets/4ddded3f-a51f-49bd-81b7-88e17d978836" />
      <br><em>Manage members</em>
    </td>
  </tr>
  <tr>
    <td align="center">
      <img width="400" alt="kép" src="https://github.com/user-attachments/assets/4eb90f7b-bc0c-4b75-a3bd-389d7fb7df33" />
      <br><em>Task Comments</em>
    </td>
  </tr>
</table>

