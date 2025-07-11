# Ticket Manager

A Spring Boot 3 RESTful backend for managing support tickets, users and devices. It ships with JWT-based authentication, role-based access control and a MySQL data store.

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
```

Edit `src/main/resources/application.properties` if you need to change the DB credentials or JWT settings.

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
spring.datasource.url=jdbc:mysql://localhost:3306/ticket_manager
spring.datasource.username=<your-mysql-user>
spring.datasource.password=<your-mysql-password>

# show SQL statements and update schema automatically (dev only)
spring.jpa.show-sql=true
spring.jpa.generate-ddl=true

# JWT
jwt.secret-key=<a-long-random-string>
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

## Running Tests

A test skeleton exists under `src/test`, but no unit tests have been implemented yet. Feel free to contribute!

## Contributing

Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## License

Distributed under the MIT License. See `LICENSE` for more information.
