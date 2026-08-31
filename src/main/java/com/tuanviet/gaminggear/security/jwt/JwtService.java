package com.tuanviet.gaminggear.security.jwt;

import com.tuanviet.gaminggear.entity.auth.Role;
import com.tuanviet.gaminggear.entity.auth.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class JwtService {

    @Value("${security.jwt.secret}")
    private String secret;

    @Value("${security.jwt.expiration}")
    private Long expiration;

    @Value("${security.jwt.refresh-expiration}")
    private Long refreshExpiration;

    public String generateAccessToken(User user){
        Date now = new Date();
        Date expiredAt = new Date(now.getTime() + expiration);

        List<String> roles = user.getRoles().stream()
                .map(Role::getName)
                .map(Enum::name)
                .toList();

        return Jwts.builder()
                .subject(user.getEmail())
                .issuedAt(now)
                .expiration(expiredAt)
                .claim("roles",roles)
                .claim("type","ACCESS")
                .signWith(getSigningKey())
                .compact();
    }

    public String generateRefreshToken(User user, UUID jti){
        Date now = new Date();
        Date expiredAt = new Date(now.getTime() + refreshExpiration);

        List<String> roles = user.getRoles().stream()
                .map(Role::getName)
                .map(Enum::name)
                .toList();

        return Jwts.builder()
                .subject(user.getEmail())
                .id(jti.toString())
                .issuedAt(now)
                .expiration(expiredAt)
                .claim("type","REFRESH")
                .signWith(getSigningKey())
                .compact();
    }

    public String extractEmail(String token){
        return extractAllClaims(token).getSubject();
    }

    public UUID extractJti(String token){
        return UUID.fromString(extractAllClaims(token).getId());
    }

    public boolean isRefreshToken(String token){
        return "REFRESH".equals(extractAllClaims(token).get("type",String.class));
    }
    public boolean isAccessToken(String token) {
        return "ACCESS".equals(
                extractAllClaims(token).get("type", String.class)
        );
    }

    public boolean isValid(String token){
        try {
            extractAllClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException exception){
            return false;
        }
    }

    public Claims extractAllClaims(String token){
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    public long getAccessTokenExpiration() {
        return expiration;
    }

    public long getRefreshTokenExpiration() {
        return refreshExpiration;
    }
}
