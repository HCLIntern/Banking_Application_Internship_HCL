package com.digitalbanking.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtTokenProvider {

  @Value("${app.jwt.secret:your_jwt_secret_key_should_be_at_least_256_bits_long_for_security}")
  private String jwtSecret;

  @Value("${app.jwt.expiration:86400000}")
  private long jwtExpiration;

  private SecretKey getSigningKey() {
    return Keys.hmacShaKeyFor(jwtSecret.getBytes());
  }

  public String generateToken(Long userId, String username) {
    return Jwts.builder()
        .subject(username)
        .claim("userId", userId)
        .issuedAt(new Date())
        .expiration(new Date(System.currentTimeMillis() + jwtExpiration))
        .signWith(getSigningKey(), Jwts.SIG.HS512)
        .compact();
  }

  public Claims getAllClaimsFromToken(String token) {
    return Jwts.parser()
        .verifyWith(getSigningKey())
        .build()
        .parseSignedClaims(token)
        .getPayload();
  }

  public String getUsernameFromToken(String token) {
    return getAllClaimsFromToken(token).getSubject();
  }

  public Long getUserIdFromToken(String token) {
    return getAllClaimsFromToken(token).get("userId", Long.class);
  }

  public boolean isTokenValid(String token) {
    try {
      Jwts.parser()
          .verifyWith(getSigningKey())
          .build()
          .parseSignedClaims(token);
      return true;
    } catch (JwtException | IllegalArgumentException e) {
      return false;
    }
  }

  public boolean isTokenExpired(String token) {
    try {
      Date expiration = getAllClaimsFromToken(token).getExpiration();
      return expiration.before(new Date());
    } catch (JwtException e) {
      return true;
    }
  }
}
