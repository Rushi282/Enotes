package com.enote.service;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.enote.entity.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService implements IJwtService {
	
//	private String secreteKey ="gfjfdhfjgfhgfuytecytdvkjdvdkfjdkxjvhfjdfvfjdfvkdjh";
	private String secreteKey = "";
	
	public JwtService() {
		try {
			KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSHA256");
			SecretKey secretKey = keyGenerator.generateKey();
			secreteKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public String generateToken(User user) {
		
		Map<String, Object> claims = new HashMap<>();
		claims.put("id", user.getId());
		claims.put("role", user.getRoles());
		claims.put("status", user.getStatus().getIsActive());
		
		String token = Jwts.builder()
		.claims().add(claims)
		.subject(user.getEmail())
		.issuedAt(new Date(System.currentTimeMillis()))
		.expiration(new Date(System.currentTimeMillis()+60*60*60*10))
		.and()
		.signWith(getKey())
		.compact();
		return token;
	}

	private Key getKey() {
		byte[] key = Decoders.BASE64.decode(secreteKey);
		return Keys.hmacShaKeyFor(key);
	}
	
	@Override
	public String extractUserName(String token) {
		Claims claims = extractClaims(token);
		return claims.getSubject();
	}
	
	private Claims extractClaims(String token) {
		try {
			return Jwts.parser()
			.verifyWith(decryptKey())
			.build()
			.parseSignedClaims(token)
			.getPayload();
		} catch (ExpiredJwtException e) {
			throw new ExpiredJwtException(null, null, "Token is expired");
		} catch (JwtException e) {
			throw new JwtException("Invalid token");
		}
	}

	private SecretKey decryptKey() {
		byte[] key = Decoders.BASE64.decode(secreteKey);
		return Keys.hmacShaKeyFor(key);
	}
	
	public String extractRole(String token) {
		Claims claims = extractClaims(token);
		String role = (String)claims.get("role");
		return role;
	}

	@Override
	public Boolean validateToken(String token, UserDetails userDetails) {
		String userName = extractUserName(token);
		Claims claims = extractClaims(token);
		Boolean isExpired = isTokenExpired(claims);
		if(userName.equalsIgnoreCase(userDetails.getUsername()) && !isExpired ) {
			return true;			
		}
		return false;
	}

	private Boolean isTokenExpired(Claims claims) {
		Date expiredDate = claims.getExpiration();
		return expiredDate.before(new Date());
	}

}
