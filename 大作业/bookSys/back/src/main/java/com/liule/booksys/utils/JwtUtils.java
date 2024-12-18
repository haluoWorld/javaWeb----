package com.liule.booksys.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtils {

    private static final Key SECRET_KEY = Keys.hmacShaKeyFor("QmRjp9v7D9jvlkfmqvBNQpU9e5Nj2kpQXk3r0@N23X5yzgT1tG3N".getBytes()); // 密钥
    private static final long EXPIRATION_TIME = 86400000; // 过期时间 24小时

    // 生成 JWT
    public static String generateToken(String username, String role) {
        Map<String, Object> claims = new HashMap<>();
        // 存储用户信息
        claims.put("role", role); //存储role

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    // 解析 JWT
    public static Claims parseToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new RuntimeException("Token has expired", e);
        } catch (JwtException e) {
            throw new RuntimeException("Invalid token", e);
        }
    }

    // 获取用户名
    public static String getUsernameFromToken(String token) {
        return parseToken(token).getSubject();
    }

    // 获取用户角色
    public static String getRoleFromToken(String token) {
        return parseToken(token).get("role", String.class);
    }

    // 检查 token 是否过期
    public static boolean isTokenExpired(String token) {
        return parseToken(token).getExpiration().before(new Date());
    }
}
