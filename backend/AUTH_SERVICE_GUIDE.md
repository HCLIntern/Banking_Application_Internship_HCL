# AuthService Implementation Guide

## Overview
`AuthServiceImpl` provides complete user authentication with JWT tokens, including login, signup, and token refresh functionality.

## Files Created/Modified

### 1. **AuthServiceImpl.java** (Service Implementation)
Location: `backend/src/main/java/com/digitalbanking/service/impl/AuthServiceImpl.java`

#### Dependencies Injected:
```java
- UserRepository          // Database access
- PasswordEncoder         // BCrypt password hashing
- JwtTokenProvider        // JWT token generation
```

---

## Method Details

### **1. login(LoginRequest) → LoginResponse**

**Purpose**: Authenticate user with credentials and return JWT token

**Flow**:
```
Input: { username: "john_doe", password: "password123" }
    ↓
1. Find user by username in database
    ↓
2. Check if user account is active
    ↓
3. Verify password (plaintext vs BCrypt hash)
    ↓
4. Generate JWT token (valid for 24 hours)
    ↓
Output: {
  token: "eyJhbGciOiJIUzUxMiJ9...",
  type: "Bearer",
  id: 1,
  username: "john_doe",
  email: "john@example.com",
  fullName: "John Doe",
  role: "CUSTOMER"
}
```

**Error Cases**:
- Username doesn't exist → "Invalid username or password"
- User account disabled → "User account is disabled"
- Wrong password → "Invalid username or password"

**Security**: Uses BCrypt's `matches()` method for secure password comparison

---

### **2. signup(SignUpRequest) → void**

**Purpose**: Register new user with validation

**Flow**:
```
Input: {
  username: "jane_smith",
  email: "jane@example.com",
  password: "SecurePass123",
  fullName: "Jane Smith",
  phone: "+1234567890"
}
    ↓
1. Validate all fields (not null, password min 6 chars)
    ↓
2. Check if username already exists
    ↓
3. Check if email already exists
    ↓
4. Hash password using BCrypt
    ↓
5. Create User entity with role=CUSTOMER, isActive=true
    ↓
6. Save to database
    ↓
Output: Success message (user should login now)
```

**Validation Rules**:
- Username: Not empty, must be unique
- Email: Not empty, must be unique
- Password: Min 6 characters
- Full Name: Not empty
- Phone: Optional

**Error Cases**:
- Username already taken
- Email already registered
- Password too short
- Empty required fields

**Security**: Password is hashed before saving to database

---

### **3. refreshToken(String token) → LoginResponse**

**Purpose**: Generate new token for authenticated user

**Flow**:
```
Input: "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9..."
    ↓
1. Remove "Bearer " prefix
    ↓
2. Validate token (signature + expiration)
    ↓
3. Extract username & userId from token
    ↓
4. Find user in database
    ↓
5. Check if user is still active
    ↓
6. Generate new token with fresh expiration
    ↓
Output: {
  token: "eyJhbGciOiJIUzUxMiJ9...",  // NEW token
  type: "Bearer",
  id: 1,
  username: "john_doe",
  email: "john@example.com",
  fullName: "John Doe",
  role: "CUSTOMER"
}
```

**Error Cases**:
- Token is expired → "Invalid or expired token"
- Token signature invalid → "Invalid or expired token"
- User not found → "User not found"
- User account disabled → "User account is disabled"

---

## API Endpoints

### **POST /api/v1/auth/login**
```bash
curl -X POST http://localhost:5000/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john_doe",
    "password": "password123"
  }'
```

**Response (Success - 200)**:
```json
{
  "success": true,
  "message": "Login successful",
  "data": {
    "token": "eyJhbGciOiJIUzUxMiJ9...",
    "type": "Bearer",
    "id": 1,
    "username": "john_doe",
    "email": "john@example.com",
    "fullName": "John Doe",
    "role": "CUSTOMER"
  },
  "timestamp": "2024-01-02T10:30:00"
}
```

**Response (Failure - 401)**:
```json
{
  "success": false,
  "message": "Invalid username or password",
  "data": null,
  "timestamp": "2024-01-02T10:30:00"
}
```

---

### **POST /api/v1/auth/signup**
```bash
curl -X POST http://localhost:5000/api/v1/auth/signup \
  -H "Content-Type: application/json" \
  -d '{
    "username": "jane_smith",
    "email": "jane@example.com",
    "password": "SecurePass123",
    "fullName": "Jane Smith",
    "phone": "+1234567890"
  }'
```

**Response (Success - 201)**:
```json
{
  "success": true,
  "message": "User registered successfully. Please login.",
  "data": null,
  "timestamp": "2024-01-02T10:30:00"
}
```

**Response (Failure - 400)**:
```json
{
  "success": false,
  "message": "Username already taken. Please choose another.",
  "data": null,
  "timestamp": "2024-01-02T10:30:00"
}
```

---

### **POST /api/v1/auth/refresh**
```bash
curl -X POST http://localhost:5000/api/v1/auth/refresh \
  -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9..."
```

**Response (Success - 200)**:
```json
{
  "success": true,
  "message": "Token refreshed successfully",
  "data": {
    "token": "eyJhbGciOiJIUzUxMiJ9...",  // NEW token
    "type": "Bearer",
    "id": 1,
    "username": "john_doe",
    "email": "john@example.com",
    "fullName": "John Doe",
    "role": "CUSTOMER"
  },
  "timestamp": "2024-01-02T10:30:00"
}
```

---

## JWT Token Structure

```
eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqb2huX2RvZSIsInVzZXJJZCI6MSwiZXhwIjoxNjcyNTMyMzQwfQ.signature

Header:   { "alg": "HS512" }
Payload:  {
  "sub": "john_doe",          // username
  "userId": 1,                // user id
  "iat": 1672448940,         // issued at
  "exp": 1672532340          // expires at (24 hours later)
}
Signature: HMAC-SHA512 signed with secret key
```

**Token Validity**: 24 hours  
**Refresh**: Call `/refresh` endpoint before expiration

---

## Security Features

### 1. **Password Hashing**
- Uses BCrypt with auto-generated salt
- Never stores plaintext passwords
- Comparison: `passwordEncoder.matches(plaintext, hash)` prevents timing attacks

### 2. **JWT Tokens**
- Signed with HMAC-SHA512
- Secret key: Min 256 bits (configured in Constants.java)
- Expiration: 24 hours
- Payload cannot be forged without secret key

### 3. **User Status Check**
- Active flag prevents disabled accounts from logging in
- Checked in login and refresh methods

### 4. **Validation**
- Input validation (non-null, length checks)
- Duplicate username/email checks
- Token signature verification

---

## Database Interaction

### **Login**
```sql
SELECT * FROM users WHERE username = 'john_doe';
```

### **Signup**
```sql
INSERT INTO users (username, email, password, full_name, phone, role, is_active, created_at, updated_at)
VALUES ('jane_smith', 'jane@example.com', '$2a$10$...hashedpassword...', 'Jane Smith', '+1234567890', 'CUSTOMER', true, NOW(), NOW());
```

### **Refresh Token**
```sql
SELECT * FROM users WHERE username = 'john_doe';
```

---

## Error Handling

| Method | Error | Status | Message |
|--------|-------|--------|---------|
| login | User not found | 401 | Invalid username or password |
| login | Account disabled | 401 | User account is disabled |
| login | Wrong password | 401 | Invalid username or password |
| signup | Username exists | 400 | Username already taken |
| signup | Email exists | 400 | Email already registered |
| signup | Invalid input | 400 | Validation error message |
| refresh | Invalid token | 401 | Invalid or expired token |
| refresh | Expired token | 401 | Invalid or expired token |
| refresh | User disabled | 401 | User account is disabled |

---

## Frontend Integration

### **Store Token** (React Context/localStorage)
```javascript
const loginResponse = await axios.post('/api/v1/auth/login', {
  username: 'john_doe',
  password: 'password123'
});

// Store token
localStorage.setItem('token', loginResponse.data.data.token);
localStorage.setItem('user', JSON.stringify(loginResponse.data.data));
```

### **Include Token in All Requests**
```javascript
const instance = axios.create({
  baseURL: 'http://localhost:5000',
  headers: {
    'Authorization': `Bearer ${localStorage.getItem('token')}`
  }
});
```

### **Refresh Token Before Expiry**
```javascript
// Run every 20 hours (token expires in 24 hours)
setInterval(() => {
  axios.post('/api/v1/auth/refresh')
    .then(response => {
      localStorage.setItem('token', response.data.data.token);
    });
}, 20 * 60 * 60 * 1000);
```

---

## Testing

### **Unit Test Example**
```java
@Test
void testLoginSuccess() {
  LoginRequest request = new LoginRequest("john_doe", "password123");
  LoginResponse response = authService.login(request);
  
  assertNotNull(response.getToken());
  assertEquals("john_doe", response.getUsername());
  assertEquals("CUSTOMER", response.getRole());
}

@Test
void testLoginInvalidPassword() {
  LoginRequest request = new LoginRequest("john_doe", "wrongpassword");
  
  assertThrows(UnauthorizedException.class, () -> authService.login(request));
}

@Test
void testSignupDuplicateUsername() {
  SignUpRequest request = new SignUpRequest("existing_user", "email@test.com", "pass123", "Name", "123");
  
  assertThrows(IllegalArgumentException.class, () -> authService.signup(request));
}
```

---

## Configuration

Update `application.yml` if needed:
```yaml
# JWT Configuration
app:
  jwt:
    secret: your_jwt_secret_key_should_be_at_least_256_bits_long_for_security
    expiration: 86400000  # 24 hours in milliseconds

# Database
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/digital_bank
    username: dev
    password: dev
```

---

## Summary

✅ **AuthServiceImpl** provides:
- Secure login with BCrypt password verification
- User registration with validation
- JWT token generation (24-hour validity)
- Token refresh functionality
- Comprehensive error handling
- Logging for debugging

Ready for production with proper security practices!
