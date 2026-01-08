# AuthService - Complete Architecture Diagram

## High-Level System Architecture

```
┌─────────────────────────────────────────────────────────────────────────┐
│                          FRONTEND (React)                               │
│                      http://localhost:5173                              │
│  ┌─────────────────────────────────────────────────────────────────┐   │
│  │ Components:                                                      │   │
│  │ ├─ Login.jsx      → POST /auth/login                           │   │
│  │ ├─ SignUp.jsx     → POST /auth/signup                          │   │
│  │ ├─ AuthContext    → Stores token & user in context             │   │
│  │ └─ ProtectedRoute → Checks token in localStorage               │   │
│  └─────────────────────────────────────────────────────────────────┘   │
└────────────────────────────────┬────────────────────────────────────────┘
                                 │
                    HTTP/HTTPS (JSON over REST)
                                 │
                                 ▼
┌─────────────────────────────────────────────────────────────────────────┐
│                         BACKEND (Spring Boot)                           │
│                      http://localhost:5000                              │
│                                                                          │
│  ┌──────────────────────────────────────────────────────────────────┐  │
│  │ LAYER 1: API Gateway & Cross-Cutting Concerns                   │  │
│  │ ├─ CorsConfig.java        → CORS middleware                     │  │
│  │ ├─ JwtAuthenticationFilter → Extracts token from requests       │  │
│  │ └─ GlobalExceptionHandler  → Centralized error handling         │  │
│  └──────────────────────────────────────────────────────────────────┘  │
│                                 │                                        │
│                                 ▼                                        │
│  ┌──────────────────────────────────────────────────────────────────┐  │
│  │ LAYER 2: REST Controllers (Presentation Layer)                  │  │
│  │ ├─ AuthController.java                                          │  │
│  │ │  ├─ POST /auth/login                                          │  │
│  │ │  │  ├─ Input validation                                       │  │
│  │ │  │  ├─ Call authService.login()                               │  │
│  │ │  │  └─ Return ApiResponse<LoginResponse>                      │  │
│  │ │  ├─ POST /auth/signup                                         │  │
│  │ │  │  ├─ Input validation                                       │  │
│  │ │  │  ├─ Call authService.signup()                              │  │
│  │ │  │  └─ Return ApiResponse with success message                │  │
│  │ │  └─ POST /auth/refresh                                        │  │
│  │ │     ├─ Extract token from Authorization header                │  │
│  │ │     ├─ Call authService.refreshToken()                        │  │
│  │ │     └─ Return ApiResponse<LoginResponse>                      │  │
│  │ └─ Error Handling (try-catch → ApiResponse.error())             │  │
│  └──────────────────────────────────────────────────────────────────┘  │
│                                 │                                        │
│                                 ▼                                        │
│  ┌──────────────────────────────────────────────────────────────────┐  │
│  │ LAYER 3: Business Logic (Service Layer)                         │  │
│  │                                                                   │  │
│  │ AuthServiceImpl.java (@Service, @Transactional)                  │  │
│  │                                                                   │  │
│  │ ┌─ login(LoginRequest) ─────────────────────────────────────┐  │  │
│  │ │ 1. userRepository.findByUsername()                        │  │  │
│  │ │ 2. Check if user.isActive                                 │  │  │
│  │ │ 3. passwordEncoder.matches(plaintext, hash)               │  │  │
│  │ │ 4. jwtTokenProvider.generateToken(userId, username)       │  │  │
│  │ │ 5. return LoginResponse                                   │  │  │
│  │ └─ Errors: UnauthorizedException                            │  │  │
│  │                                                              │  │  │
│  │ ┌─ signup(SignUpRequest) ───────────────────────────────────┐  │  │
│  │ │ 1. Validate fields (non-null, length)                     │  │  │
│  │ │ 2. Check !userRepository.existsByUsername()               │  │  │
│  │ │ 3. Check !userRepository.existsByEmail()                  │  │  │
│  │ │ 4. hashedPassword = passwordEncoder.encode()              │  │  │
│  │ │ 5. User.builder() → set all fields                        │  │  │
│  │ │ 6. userRepository.save(user)                              │  │  │
│  │ │ 7. return void (user saved)                               │  │  │
│  │ └─ Errors: IllegalArgumentException                         │  │  │
│  │                                                              │  │  │
│  │ ┌─ refreshToken(token) ─────────────────────────────────────┐  │  │
│  │ │ 1. Remove "Bearer " prefix                                │  │  │
│  │ │ 2. jwtTokenProvider.isTokenValid(token)                   │  │  │
│  │ │ 3. Extract username = jwtTokenProvider.getUsernameFromToken  │  │
│  │ │ 4. Extract userId = jwtTokenProvider.getUserIdFromToken   │  │  │
│  │ │ 5. userRepository.findByUsername()                        │  │  │
│  │ │ 6. Check user.isActive                                    │  │  │
│  │ │ 7. jwtTokenProvider.generateToken(userId, username)       │  │  │
│  │ │ 8. return LoginResponse                                   │  │  │
│  │ └─ Errors: UnauthorizedException, ResourceNotFoundException │  │  │
│  └──────────────────────────────────────────────────────────────────┘  │
│                                 │                                        │
│                    ┌────────────┼────────────┐                          │
│                    │            │            │                          │
│                    ▼            ▼            ▼                          │
│  ┌──────────────┐  ┌──────────────────┐  ┌──────────────────┐        │  │
│  │ Repository   │  │ Password Encoder │  │ JWT Provider     │        │  │
│  │              │  │                  │  │                  │        │  │
│  │UserRepository│  │BCryptPassword    │  │JwtTokenProvider  │        │  │
│  │              │  │Encoder           │  │                  │        │  │
│  │Methods:      │  │                  │  │Methods:          │        │  │
│  │- findByUser- │  │Methods:          │  │- generateToken() │        │  │
│  │  name()      │  │- encode()        │  │- isTokenValid()  │        │  │
│  │- existsByUser│  │- matches()       │  │- getUsernameFrom │        │  │
│  │  name()      │  │                  │  │  Token()         │        │  │
│  │- findByEmail │  │Cost Factor: 10   │  │- getUserIdFromTo │        │  │
│  │- existsByEmail               │  │  │ken()             │        │  │
│  │- save()      │  │BCrypt Hash:      │  │- getAllClaims()  │        │  │
│  │              │  │$2a$10$...        │  │- isTokenExpired()│        │  │
│  └──────────────┘  └──────────────────┘  └──────────────────┘        │  │
│         │                    │                     │                   │  │
│         └────────────────────┼─────────────────────┘                   │  │
│                              │                                         │  │
│                              ▼                                         │  │
│  ┌──────────────────────────────────────────────────────────────────┐  │
│  │ LAYER 4: Data Access Layer                                       │  │
│  │                                                                   │  │
│  │ UserRepository.java (extends JpaRepository)                      │  │
│  │ ├─ findByUsername(String) → Optional<User>                      │  │
│  │ ├─ findByEmail(String) → Optional<User>                         │  │
│  │ ├─ existsByUsername(String) → boolean                           │  │
│  │ ├─ existsByEmail(String) → boolean                              │  │
│  │ └─ save(User) → User                                            │  │
│  │                                                                   │  │
│  │ Uses Spring Data JPA to generate SQL automatically               │  │
│  └──────────────────────────────────────────────────────────────────┘  │
└────────────────────────────────┬────────────────────────────────────────┘
                                 │
                            JDBC/SQL
                                 │
                                 ▼
┌─────────────────────────────────────────────────────────────────────────┐
│                          DATABASE                                        │
│                      (PostgreSQL)                                        │
│                  localhost:5432                                          │
│                                                                          │
│  Table: users                                                            │
│  ┌────────────────────────────────────────────────────────────────┐   │
│  │ Column         │ Type              │ Notes                      │   │
│  ├────────────────┼───────────────────┼────────────────────────────┤   │
│  │ id             │ SERIAL PRIMARY KEY│ Auto-generated             │   │
│  │ username       │ VARCHAR(100)      │ UNIQUE, NOT NULL           │   │
│  │ email          │ VARCHAR(150)      │ UNIQUE, NOT NULL           │   │
│  │ password       │ VARCHAR(255)      │ BCrypt hash (never plain)  │   │
│  │ full_name      │ VARCHAR(200)      │ Not null                   │   │
│  │ phone          │ VARCHAR(20)       │ Optional                   │   │
│  │ role           │ VARCHAR(20)       │ CUSTOMER or ADMIN          │   │
│  │ is_active      │ BOOLEAN           │ true/false                 │   │
│  │ created_at     │ TIMESTAMP         │ Default NOW()              │   │
│  │ updated_at     │ TIMESTAMP         │ Default NOW()              │   │
│  └────────────────────────────────────────────────────────────────┘   │
│                                                                          │
│  Indexes:                                                                │
│  ├─ idx_users_username   → Fast username lookups                       │
│  └─ idx_users_email      → Fast email lookups                          │
└────────────────────────────────────────────────────────────────────────┘
```

---

## Request-Response Flow Diagram

### **Login Flow**

```
FRONTEND (Client)                BACKEND (Spring Boot)                DATABASE

User clicks Login
    │
    ├─ Inputs: { username, password }
    │
    └─> HTTP POST /auth/login
            │
            ├─ Headers: { Content-Type: application/json }
            ├─ Body: { username: "john_doe", password: "password123" }
            │
            └─────────────────────────────────────────────────────────────┐
                                                                            │
                                                        CorsConfig checks origin
                                                                            │
                                        AuthController.login() receives
                                        ├─ Validates @RequestBody
                                        ├─ Calls authService.login(request)
                                        │
                                        └──> AuthServiceImpl.login()
                                            ├─ userRepository.findByUsername()
                                            │   └─ Query: SELECT * FROM users
                                            │       WHERE username = 'john_doe'
                                            │       │
                                            │       └──────────────────────────┐
                                            │                                   │
                                            │   <────── Returns User record ────┤
                                            │
                                            ├─ Check user.isActive == true
                                            │   ✓ Pass
                                            │
                                            ├─ passwordEncoder.matches()
                                            │   ├─ Input: "password123" (plaintext)
                                            │   ├─ Hash stored: "$2a$10$..."
                                            │   └─ Uses BCrypt algorithm to compare
                                            │       ✓ Match (passwords identical)
                                            │
                                            ├─ jwtTokenProvider.generateToken()
                                            │   ├─ Creates header: { alg: HS512 }
                                            │   ├─ Creates payload: { sub: john_doe, userId: 1, exp: tomorrow }
                                            │   ├─ Generates signature using secret key
                                            │   └─ Returns: "eyJhbGciOiJIUzUxMiJ9..."
                                            │
                                            └─ return LoginResponse(
                                                token, id, username, email, role
                                              )
                                        │
                                        └─ Catches exceptions → ApiResponse.error()
                                        │
                                        └─ return ResponseEntity(ApiResponse)
            │
            <─────────────────────────────────────────────────────────────
            │
            │ HTTP 200 OK
            ├─ Headers: { Content-Type: application/json }
            ├─ Body: {
            │   "success": true,
            │   "message": "Login successful",
            │   "data": {
            │     "token": "eyJhbGciOiJIUzUxMiJ9...",
            │     "type": "Bearer",
            │     "id": 1,
            │     "username": "john_doe",
            │     "email": "john@example.com",
            │     "fullName": "John Doe",
            │     "role": "CUSTOMER"
            │   }
            │ }
            │
            <─ Response received
            │
            ├─ localStorage.setItem('token', token)
            ├─ localStorage.setItem('user', JSON.stringify(user))
            │
            └─ Redirect to /dashboard
```

---

## Data Structure Diagram

### **User Entity (Database)**

```
┌─────────────────────────────────────────┐
│          User Entity                    │
├─────────────────────────────────────────┤
│ id: Long                 (1)            │
│ username: String         ("john_doe")   │
│ email: String            ("john@ex.com")│
│ password: String         ("$2a$10$...")│ ← BCrypt hash
│ fullName: String         ("John Doe")  │
│ phone: String            ("+123...")   │
│ role: UserRole           (CUSTOMER)    │
│ isActive: boolean        (true)        │
│ createdAt: LocalDateTime (2024-01-01) │
│ updatedAt: LocalDateTime (2024-01-02) │
└─────────────────────────────────────────┘
```

### **JWT Token (Claims)**

```
┌──────────────────────────────────────────┐
│         JWT Token Claims                 │
├──────────────────────────────────────────┤
│ sub: String              ("john_doe")   │
│ userId: Long             (1)             │
│ iat: Long timestamp      (1672448940)   │
│ exp: Long timestamp      (1672532340)   │ ← 24 hours later
│ alg: String              ("HS512")      │
│ typ: String              ("JWT")        │
└──────────────────────────────────────────┘
```

### **LoginResponse DTO**

```
┌──────────────────────────────────────────┐
│      LoginResponse (DTO)                 │
├──────────────────────────────────────────┤
│ token: String             ("eyJ...")     │
│ type: String              ("Bearer")    │
│ id: Long                  (1)            │
│ username: String          ("john_doe")  │
│ email: String             ("john@...")  │
│ fullName: String          ("John Doe")  │
│ role: String              ("CUSTOMER")  │
└──────────────────────────────────────────┘
```

---

## Security Flow Diagram

```
REQUEST ARRIVES AT BACKEND
    │
    ▼
┌─────────────────────────────────────────┐
│ CorsConfig.java                         │
│ Check if origin is allowed              │
└──────────────┬──────────────────────────┘
               │
               ├─ Allowed: localhost:5173 ✓
               └─ continue
               │
               ▼
┌──────────────────────────────────────────┐
│ JwtAuthenticationFilter.java             │
│ Extract Authorization header            │
└──────────────┬───────────────────────────┘
               │
               ├─ Has "Authorization: Bearer"? 
               │   ├─ Yes: Extract token
               │   └─ No: Continue (might be /auth/login endpoint)
               │
               ▼
┌──────────────────────────────────────────┐
│ JwtTokenProvider.isTokenValid()          │
│ Verify token signature & expiration      │
└──────────────┬───────────────────────────┘
               │
               ├─ Signature matches? ✓
               ├─ Not expired? ✓
               └─ Username found? ✓
               │
               ▼
┌──────────────────────────────────────────┐
│ SecurityContext.setAuthentication()      │
│ Set authenticated user in context        │
└──────────────┬───────────────────────────┘
               │
               ▼
┌──────────────────────────────────────────┐
│ @RoleBasedAccess annotation checks       │
│ Does user have required role?            │
└──────────────┬───────────────────────────┘
               │
               ├─ /admin/* → requires ROLE_ADMIN
               ├─ /auth/* → permitAll()
               └─ Other → authenticated()
               │
               ▼
ENDPOINT EXECUTES with authenticated user context
```

---

## Error Handling Flow

```
EXCEPTION OCCURS
    │
    ▼
┌─────────────────────────────┐
│ Check Exception Type        │
└──────────┬──────────────────┘
           │
    ┌──────┼──────┬──────────┐
    │      │      │          │
    ▼      ▼      ▼          ▼
  UNAUTH  ILLEGAL RESOURCE  GENERIC
  IZED    ARG      NOT       EXCEPTION
          FOUND
    │      │      │          │
    ▼      ▼      ▼          ▼
  401    400    404        500
  Unauthorized  BadRequest NotFound InternalError
    │      │      │          │
    └──────┴──────┴──────────┘
           │
           ▼
┌─────────────────────────────────────┐
│ GlobalExceptionHandler.java         │
│ handleException() method             │
└──────────┬────────────────────────────┘
           │
           ▼
┌──────────────────────────────────────┐
│ Build ApiResponse:                   │
│ {                                    │
│   "success": false,                  │
│   "message": "Error description",    │
│   "data": null,                      │
│   "timestamp": "2024-01-02..."       │
│ }                                    │
└──────────┬────────────────────────────┘
           │
           ▼
┌──────────────────────────────────────┐
│ Return ResponseEntity with status    │
│ and error response body              │
└──────────────────────────────────────┘
```

---

## Component Interaction Matrix

```
                    │  UserRepo  │  PassEncoder  │  JwtProvider  │  UserEntity  │  DB
────────────────────┼────────────┼───────────────┼───────────────┼──────────────┼────
login()             │     ✓      │       ✓       │       ✓       │              │  ✓
signup()            │     ✓      │       ✓       │               │              │  ✓
refreshToken()      │     ✓      │               │       ✓       │              │  ✓
passwordEncoder     │            │       ✓       │               │              │
jwtTokenProvider    │            │               │       ✓       │              │
────────────────────┴────────────┴───────────────┴───────────────┴──────────────┴────

✓ = Direct usage/interaction
```

---

This completes the **AuthService implementation** with comprehensive documentation!
