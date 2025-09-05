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

Backend
- **Spring Boot 3**
- **Spring MVC**
- **Spring Security 6**
- **Spring Data JPA**
- **JWT Token Authentication** 
- **JUnit**
- **Lombok**
- **MySQL**
- **Liquibase**
- **Docker**

Frontend
- **Angular**
- **Angular Material**
- **RxJS**
- **HTTP Interceptors**
- **Bootstrap**


## Setup & Installation

### Prerequisites
- Java 17
- Node.js (currently using v22.17.1)
- Angular CLI (currently using v20.0.3)
- Docker (currently using v28.3.2)

### Configuration
Add an `.env` file in the /backend folder with the following configuration:

```env
MYSQL_PASSWORD=your-docker-db-password
MYSQL_HOST=your-docker-db-host
MYSQL_DATABASE=your-database-host
KEYCLOAK_ADMIN=your-database-admin-name
KEYCLOAK_PASSWORD=your-database-admin-password
```
Both Keycloak and MySQL will be running in a docker container, so feel free to use 
whatever values you want here. The compose file will read the values from the .env file.
### Run

**Backend:**

- Start the database and keycloak container
```bash
cd backend
docker compose up
```
- Start the spring boot application
```bash
./mvnw spring-boot:run
```

**Frontend:**
```bash
cd ..
cd frontend
npm i
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

