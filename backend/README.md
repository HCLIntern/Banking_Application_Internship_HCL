# Digital Banking Backend - Spring Boot

A comprehensive Spring Boot REST API for a Digital Banking Platform with authentication, accounts, transactions, loans, payments, and assurance management.

## Project Structure

```
backend/
├── src/main/java/com/digitalbanking/
│   ├── config/           # Spring configuration classes
│   ├── controller/       # REST controllers
│   ├── service/          # Business logic interfaces
│   ├── entity/           # JPA entities
│   ├── dto/              # Data Transfer Objects
│   ├── repository/       # Data access layer
│   ├── security/         # JWT & security
│   ├── exception/        # Exception handling
│   └── util/             # Utility classes
├── src/main/resources/
│   ├── application.yml   # Application properties
│   └── db/migration/     # Flyway migrations
└── pom.xml              # Maven dependencies
```

## Features

✅ **User Authentication** - JWT-based login/signup  
✅ **Account Management** - Create and manage bank accounts  
✅ **Transactions** - Transfer funds between accounts  
✅ **Payments** - Record and track payments  
✅ **Loans** - Apply for loans and track EMI  
✅ **Assurance** - Manage insurance policies  
✅ **Admin Dashboard** - Administrative controls  
✅ **Role-Based Access** - CUSTOMER & ADMIN roles  
✅ **Database Migrations** - Flyway migrations  
✅ **Exception Handling** - Global error handling  

## Prerequisites

- **Java 17+**
- **Maven 3.8+**
- **PostgreSQL 13+**
- **Git**

## Installation & Setup

### 1. Clone & Navigate
```bash
cd backend
```

### 2. Configure Database
Update `src/main/resources/application.yml`:
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/digital_bank
    username: dev
    password: dev
```

Or start PostgreSQL via Docker:
```bash
cd ../database
docker-compose up -d
```

### 3. Build Project
```bash
mvn clean install
```

### 4. Run Application
```bash
mvn spring-boot:run
```

Server runs on `http://localhost:5000`

## API Endpoints

### Authentication (`/api/v1/auth/`)
- `POST /login` - Login user
- `POST /signup` - Register new user
- `POST /refresh` - Refresh JWT token

### Accounts (`/api/v1/accounts/`)
- `GET /` - List user accounts
- `GET /{id}` - Get account details
- `POST /` - Create new account
- `GET /{id}/balance` - Get account balance

### Transactions (`/api/v1/transactions/`)
- `GET /` - List transactions
- `POST /transfer` - Transfer funds
- `GET /account/{accountId}` - Account transactions

### Payments (`/api/v1/payments/`)
- `POST /` - Create payment
- `GET /` - Payment history
- `PUT /{id}/status` - Update payment status

### Loans (`/api/v1/loans/`)
- `GET /` - User's loans
- `POST /apply` - Apply for loan
- `PUT /{id}/approve` - Approve loan
- `PUT /{id}/reject` - Reject loan

### Assurance (`/api/v1/assurance/`)
- `GET /my-policies` - User policies
- `POST /apply` - Apply for policy
- `PUT /{id}/renew` - Renew policy

### Admin (`/api/v1/admin/`)
- `GET /dashboard` - Dashboard stats
- `GET /users` - All users
- `PUT /users/{id}/disable` - Disable user

## Project Flow: Next Steps

1. **Implement Service Layer** - `impl/` packages with business logic
2. **Database Migrations** - Run Flyway migrations
3. **Integration Testing** - Write unit/integration tests
4. **API Documentation** - Add Swagger/OpenAPI
5. **Frontend Integration** - Connect with React frontend on `http://localhost:5173`

## Environment Configuration

Create `application-dev.yml` for development:
```yaml
spring:
  jpa:
    show-sql: true
  datasource:
    url: jdbc:postgresql://localhost:5432/digital_bank_dev
```

## Troubleshooting

- **Database Connection Failed**: Ensure PostgreSQL is running and credentials are correct
- **JWT Errors**: Update `jwtSecret` in `Constants.java` (min 256-bit key)
- **CORS Issues**: Check `CorsConfig.java` for allowed origins

## Technologies Used

- **Spring Boot 3.2.0**
- **Spring Security + JWT**
- **Spring Data JPA**
- **PostgreSQL**
- **Flyway** (Database Migrations)
- **Lombok**
- **Maven**

## License

MIT
