# AuthService Quick Reference Card

## Core Methods at a Glance

### **login(LoginRequest)**
```java
Input:  { username: "john_doe", password: "password123" }
Output: { token: "jwt...", id: 1, username: "john_doe", role: "CUSTOMER" }

Steps:
1. Find user by username
2. Check if user.isActive == true
3. Verify password with BCrypt.matches()
4. Generate JWT token (24-hour expiry)
5. Return LoginResponse

Errors:
- UnauthorizedException: invalid username/password/disabled account
```

### **signup(SignUpRequest)**
```java
Input:  { username: "jane", email: "jane@test.com", password: "pass123", fullName: "Jane", phone: "123" }
Output: void (success message)

Steps:
1. Validate fields (non-null, password ≥6 chars)
2. Check username not duplicate
3. Check email not duplicate
4. Hash password with BCrypt
5. Create User entity (role=CUSTOMER, isActive=true)
6. Save to database

Errors:
- IllegalArgumentException: validation failures, duplicates
```

### **refreshToken(String token)**
```java
Input:  "Bearer eyJhbGciOiJIUzUxMiJ9..."
Output: { token: "new_jwt...", id: 1, username: "john_doe", role: "CUSTOMER" }

Steps:
1. Remove "Bearer " prefix
2. Validate token (signature + expiration)
3. Extract username & userId from token claims
4. Find user in database
5. Check user.isActive == true
6. Generate NEW token with fresh expiration
7. Return LoginResponse

Errors:
- UnauthorizedException: invalid/expired token, disabled account
```

---

## API Endpoints Quick Reference

```
POST /api/v1/auth/login
├─ Request:  { "username": "...", "password": "..." }
├─ Response: 200 OK with token + user details
└─ Error:    401 Unauthorized with message

POST /api/v1/auth/signup
├─ Request:  { "username": "...", "email": "...", "password": "...", "fullName": "...", "phone": "..." }
├─ Response: 201 Created with success message
└─ Error:    400 Bad Request with validation message

POST /api/v1/auth/refresh
├─ Header:   Authorization: Bearer <token>
├─ Response: 200 OK with new token
└─ Error:    401 Unauthorized
```

---

## Dependencies Injected

| Dependency | Purpose |
|-----------|---------|
| `UserRepository` | Find/save users in database |
| `PasswordEncoder` | BCrypt password hashing & verification |
| `JwtTokenProvider` | Generate & validate JWT tokens |

---

## Key Security Points

✅ **Never** store plaintext passwords  
✅ **Always** use BCrypt for hashing  
✅ **Always** validate password with `matches(plaintext, hash)`  
✅ **Always** check user.isActive before allowing login  
✅ **Never** reveal which field is invalid (use generic error message)  
✅ **Always** sign JWT with secret key  
✅ **Always** verify token signature before using  
✅ **Always** check token expiration  

---

## Exception Handling

```
UnauthorizedException
├─ Login failed: user not found
├─ Login failed: password invalid
├─ Login failed: account disabled
├─ Token invalid/expired
└─ User disabled during refresh

IllegalArgumentException (Signup)
├─ Username empty
├─ Email empty
├─ Password < 6 characters
├─ Full name empty
├─ Username already taken
└─ Email already registered

ApiResponse Wrapper
├─ Success: { "success": true, "message": "...", "data": {...} }
└─ Error:   { "success": false, "message": "...", "data": null }
```

---

## JWT Token Claims

```
{
  "sub": "john_doe",           // Username (subject)
  "userId": 1,                 // User ID
  "iat": 1672448940,          // Issued at timestamp
  "exp": 1672532340           // Expiration timestamp (24 hours later)
}
```

---

## Database Queries Generated

**Login:**
```sql
SELECT * FROM users WHERE username = 'john_doe';
-- Checks: password hash, is_active flag
```

**Signup:**
```sql
SELECT COUNT(*) FROM users WHERE username = 'jane' OR email = 'jane@test.com';
INSERT INTO users (username, email, password, full_name, phone, role, is_active, created_at, updated_at)
VALUES ('jane', 'jane@test.com', '$2a$10$hashedPassword...', 'Jane', '+1234567890', 'CUSTOMER', true, NOW(), NOW());
```

---

## Frontend Integration Pattern

```javascript
// 1. Login
const response = await axios.post('/api/v1/auth/login', {
  username: 'john_doe',
  password: 'password123'
});

const { token, user } = response.data.data;

// 2. Store token
localStorage.setItem('token', token);
localStorage.setItem('user', JSON.stringify(user));

// 3. Use token in future requests
const instance = axios.create({
  headers: {
    'Authorization': `Bearer ${localStorage.getItem('token')}`
  }
});

// 4. Refresh before expiry (every 20 hours for 24-hour token)
const refreshToken = async () => {
  const response = await instance.post('/api/v1/auth/refresh');
  localStorage.setItem('token', response.data.data.token);
};
```

---

## Testing Quick Commands

```bash
# Run all AuthService tests
mvn test -Dtest=AuthServiceTest

# Run specific test
mvn test -Dtest=AuthServiceTest#testLoginSuccess

# Run with coverage
mvn test jacoco:report

# Run integration tests (if available)
mvn verify
```

---

## Common Errors & Solutions

| Error | Cause | Solution |
|-------|-------|----------|
| "Invalid username or password" | User not found or password wrong | Check credentials |
| "User account is disabled" | Account flagged as inactive | Contact support or admin |
| "Username already taken" | Username exists | Choose different username |
| "Password must be at least 6 characters" | Password too short | Use stronger password |
| "Invalid or expired token" | Token expired or tampered | Call /refresh or re-login |
| "CORS error" | Frontend origin not allowed | Check CorsConfig.java |
| "JWT signature does not match" | Secret key changed | Ensure same key in config |

---

## Performance Optimization Notes

- **BCrypt Cost Factor**: Default 10 (adjustable in PasswordEncoder)
  - Higher = slower login but more secure
  - Recommended: 10-12 for balance

- **Token Expiration**: 24 hours (adjustable in Constants.java)
  - Shorter = more frequent refreshes (security)
  - Longer = better UX (fewer refreshes)
  - Recommended: 24 hours + refresh token rotation

- **Database Indexes**:
  ```sql
  CREATE INDEX idx_users_username ON users(username);
  CREATE INDEX idx_users_email ON users(email);
  ```
  - Speeds up login queries significantly

---

## Checklists

### Before Deploying to Production

- [ ] JWT secret key is strong (256+ bits)
- [ ] Password hashing cost factor ≥ 10
- [ ] CORS configured for production domains only
- [ ] HTTPS enabled (never HTTP for auth)
- [ ] Password requirements enforced (min length, complexity)
- [ ] Rate limiting implemented (prevent brute-force)
- [ ] Account lockout after N failed attempts
- [ ] Logging configured (track auth events)
- [ ] Error messages don't leak sensitive info
- [ ] All tests passing
- [ ] Database backups configured

### For Future Enhancements

- [ ] Email verification on signup
- [ ] Two-Factor Authentication (2FA)
- [ ] Password reset via email
- [ ] OAuth2 integration
- [ ] Refresh token rotation
- [ ] Login history tracking
- [ ] IP-based rate limiting
- [ ] Admin user management endpoints

---

## Files You Need to Know

| File | What It Does |
|------|--------------|
| `AuthServiceImpl.java` | Main business logic |
| `AuthController.java` | REST endpoints |
| `JwtTokenProvider.java` | Token generation |
| `UserRepository.java` | Database queries |
| `User.java` | User entity model |
| `AUTH_SERVICE_GUIDE.md` | Detailed documentation |
| `AUTH_SERVICE_FLOW_DIAGRAMS.md` | Visual diagrams |
| `AuthServiceTest.java` | Unit tests |

---

## One-Minute Summary

The **AuthService** handles:
1. **Login** - Authenticates user, returns JWT token
2. **Signup** - Registers new user with validation
3. **Refresh** - Generates new token for active sessions

Uses **BCrypt** for password security and **JWT** for stateless authentication. Ready for production with comprehensive error handling and tests.

---

**Ready to move to AccountService?** 🚀
