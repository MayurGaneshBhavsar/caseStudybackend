package com.library.gateway;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

	@Value("${jwt.secret}")
	private String secret;

	private SecretKey getKey() {
		return Keys.hmacShaKeyFor(secret.getBytes());
	}

	public Claims extractClaims(String token) {
		return Jwts.parserBuilder()
				.setSigningKey(getKey())
				.build()
				.parseClaimsJws(token)
				.getBody();
	}

	public String extractRole(String token) {
		return extractClaims(token).get("role", String.class);
	}

	public boolean isTokenValid(String token) {
		try {
			extractClaims(token);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
