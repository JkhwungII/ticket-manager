# Ticket Manager backend

A Spring Boot 3 RESTful backend for Ticket manager. It ships with JWT-based authentication, role-based access control and a MySQL data store.

## Features

* User authentication / JWT login
* Role-based endpoints for **admin**, **staff** and **regular users**
* CRUD APIs for Tickets, Ticket Information, Devices and Users
* Spring Data JPA persistence with MySQL
* Built using Java 17 and Maven

## Tech Stack

| Layer | Technology |
|-------|------------|
| Language | Java 17 |
| Framework | Spring Boot 3.4, Spring MVC |
| Persistence | Spring Data JPA, Hibernate, MySQL |
| Security | Spring Security 6 + JSON Web Tokens (jjwt 0.12) |
| Build | Maven |

## Prerequisites

1. Java 17+
2. Maven 3.9+
3. MySQL 8+ with an empty database named `ticket_manager`

```sql
CREATE DATABASE ticket_manager CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE TABLE device (
    id INT AUTO_INCREMENT PRIMARY KEY,
    type VARCHAR(255),
    CPU VARCHAR(255),
    mother_board VARCHAR(255),
    drive VARCHAR(255),
    additional_info TEXT
);

CREATE TABLE user_accounts (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    role VARCHAR(255),
    password VARCHAR(255),
    phone_number VARCHAR(30)
);

CREATE TABLE ticket_detail (
    id INT AUTO_INCREMENT PRIMARY KEY,
    ticket_id INT,
    staff_id INT,
    process_detail TEXT,
    created_timed DATETIME,
    FOREIGN KEY (staff_id) REFERENCES user_accounts(id) ON UPDATE RESTRICT ON DELETE RESTRICT,
    FOREIGN KEY (ticket_id) REFERENCES ticket(id) ON UPDATE RESTRICT ON DELETE RESTRICT
);

CREATE TABLE ticket (
    id INT AUTO_INCREMENT PRIMARY KEY,
    issuer_id INT,
    issue TEXT,
    device_id INT,
    created_time DATETIME,
    state VARCHAR(20),
    FOREIGN KEY (issuer_id) REFERENCES user_accounts(id) ON UPDATE RESTRICT ON DELETE CASCADE,
    FOREIGN KEY (device_id) REFERENCES device(id) ON UPDATE RESTRICT ON DELETE RESTRICT
);

```

Set enviormental variable MYSQL_URL,MYSQL_USER,MYSQL_SECRET,JWT_KEY to the respective value before starting

## Getting Started

Clone and run the project:

```bash
# build the jar
mvn clean package

# run the application (defaults to port 8080)
java -jar target/ticket_manager-0.0.1-SNAPSHOT.jar
```

The API root will now be available at `http://localhost:8080/api`.

## Environment Configuration

```
spring.application.name=ticket_manager
spring.datasource.url=${MYSQL_URL}
spring.datasource.username=${MYSQL_USER}
spring.datasource.password=${MYSQL_SECRET}

# show SQL statements and update schema automatically (dev only)
spring.jpa.show-sql=true
spring.jpa.generate-ddl=true

# JWT
jwt.secret-key=${JWT_KEY}
jwt.valid-seconds=7200
```

## API Reference

### Authentication & Users  `/api/user`

| Method | Path | Access | Description |
|--------|------|--------|-------------|
| POST | `/login` | Public | Log in and receive JWT |
| POST | `/admin` | Admin | Create a new user |
| GET | `/staff/userinfo/{user_id}` | Staff | Retrieve user info |

### Tickets  `/api/tickets`

| Method | Path | Access | Description |
|--------|------|--------|-------------|
| POST | `/ticket` | Authenticated | Create ticket |
| POST | `/staff/TicketInfo` | Staff | Append ticket commentary |
| GET | `/user/{user_ID}` | Owner | List tickets for a user |
| GET | `/staff/TicketList` | Staff | List all active tickets |
| GET | `/staff/{ticketID}` | Staff | Ticket details (staff view) |
| GET | `/staff/{ticketID}/raw` | Staff | Raw ticket entity |
| GET | `/TicketProgress/{ticketID}` | Authenticated | Progress string |
| PATCH | `/staff/Ticket/{ticket_ID}` | Staff | Update a ticket |
| PATCH | `/staff/TicketInfo/{ticketInfoID}` | Staff | Update ticket info |
| DELETE | `/admin/Ticket/{ticket_ID}` | Admin | Remove ticket |

### Devices  `/api/devices`

| Method | Path | Access | Description |
|--------|------|--------|-------------|
| POST | `/admin/device` | Admin | Register device |
| GET | `/staff/device/{Device_ID}` | Staff | Fetch device by ID |
| PATCH | `/admin/device` | Admin | Update device |
| DELETE | `/admin/device/{Device_ID}` | Admin | Delete device |


