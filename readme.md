# Expense Tracker Backend

A secure backend service for tracking personal expenses with user authentication and analytics.

## Tech Stack
- Java
- Spring Boot
- Spring Security (JWT)
- Spring Data JPA
- MySQL
- Maven

## Features
- User registration & login (JWT-based auth)
- Secure expense CRUD operations
- User-scoped data access
- Pagination support
- Monthly expense summary with category breakdown
- Global exception handling
- Validation with meaningful error responses

## API Overview
- POST /users
- POST /users/login
- POST /expenses
- GET /expenses
- PUT /expenses/{id}
- DELETE /expenses/{id}
- GET /expenses/summary?year=&month=

## How to Run
1. Clone the repo
2. Configure MySQL & application.properties
3. Run `mvn spring-boot:run`

## Future Improvements
- Soft delete
- CSV export
- Expense limits & alerts
- Dockerization
