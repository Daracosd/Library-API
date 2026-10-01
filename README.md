# Library Management API

A Spring Boot REST API for managing a small library system. The application supports catalog management, member management, and book checkout/return workflows.

## Features

- Manage books and their availability
- Manage library members
- Check out books to members
- Return borrowed books
- Detect overdue loans
- Prevent checkout for suspended members or unavailable books
- Store data in an in-memory H2 database
- Expose the H2 console for quick inspection during development

## Tech Stack

- Java 17
- Spring Boot 4.1.1
- Spring Web MVC
- Spring Data JPA
- Hibernate
- H2 in-memory database
- Maven

## Project Structure

```text
src/
  main/
    java/
      com/example/demo/
        controller/      # REST controllers
        dto/            # request payloads
        exception/      # custom exceptions and global error handling
        model/          # JPA entities
        repository/     # repository interfaces
        service/        # business logic
    resources/
      application.properties
  test/
    java/
      com/example/demo/
        service/        # service tests
```

## Prerequisites

- Java 17 or newer
- Maven 3.9+

## Getting Started

From the project root:

```bash
./mvnw clean install
./mvnw spring-boot:run
```

On Windows, you can also use:

```powershell
mvnw.cmd clean install
mvnw.cmd spring-boot:run
```

The API will start on:

```text
http://localhost:8080
```

The H2 database console is available at:

```text
http://localhost:8080/h2-console
```

Use the following JDBC settings in the H2 console:

```text
JDBC URL: jdbc:h2:mem:libarydb
User: sa
Password: (leave blank)
```

## Core Business Rules

- A member cannot check out a book while suspended.
- A book cannot be checked out if no copies are available.
- Each loan has a due date set to 14 days from the checkout date.
- Returning a loan increments the book's available copy count.
- Overdue loans are identified as loans whose due date has passed and that have not been returned.

## API Endpoints

### Books

```http
GET /books
GET /books/{id}
GET /books?author={author}
GET /books?publisher={publisher}
POST /books
PUT /books/{id}
DELETE /books/{id}
```

Example book payload:

```json
{
  "title": "The Pragmatic Programmer",
  "author": "Andrew Hunt",
  "publisher": "Addison-Wesley",
  "isbn": "9780201616224",
  "year": 1999,
  "price": 35.99,
  "totalCopies": 5,
  "availableCopies": 5
}
```

### Members

```http
GET /members
GET /members/{id}
POST /members
PUT /members/{id}
DELETE /members/{id}
```

Example member payload:

```json
{
  "name": "Jane Doe",
  "email": "jane@example.com",
  "phone": "5551234567",
  "address": "123 Main St",
  "membershipDate": "2026-01-15",
  "memberStatus": "ACTIVE"
}
```

### Loans

```http
GET /loans
GET /loans/{id}
GET /loans/overdue
POST /loans
PUT /loans/{id}/return
PUT /loans/{id}
DELETE /loans/{id}
```

Checkout request example:

```json
{
  "memberId": 1,
  "bookId": 2
}
```

## Example Requests

### Create a book

```bash
curl -X POST http://localhost:8080/books \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Clean Code",
    "author": "Robert C. Martin",
    "publisher": "Prentice Hall",
    "isbn": "9780132350884",
    "year": 2008,
    "price": 42.50,
    "totalCopies": 3,
    "availableCopies": 3
  }'
```

### Create a member

```bash
curl -X POST http://localhost:8080/members \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Alice Johnson",
    "email": "alice@example.com",
    "phone": "5559876543",
    "address": "456 Oak Ave",
    "membershipDate": "2026-02-10",
    "memberStatus": "ACTIVE"
  }'
```

### Check out a book

```bash
curl -X POST http://localhost:8080/loans \
  -H "Content-Type: application/json" \
  -d '{
    "memberId": 1,
    "bookId": 1
  }'
```

### Return a book

```bash
curl -X PUT http://localhost:8080/loans/1/return
```

## Running Tests

```bash
./mvnw test
```

## Notes

This project is a simple library domain example designed to demonstrate REST API patterns, JPA, validation, and exception handling in a Spring Boot application.
