package com.edu.messengerrelex.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtTokenUtil {
    final SecretKey key = Jwts.SIG.HS256.key().build(); // Ключ для кодирования
    final int timeLifeMs = 5000000;

    // Генерация токена
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder()
                .claims(claims)
                .subject(userDetails.getUsername()) // Добавления login
                .issuedAt(new Date(System.currentTimeMillis())) // Время создания
                .expiration(new Date(System.currentTimeMillis() + timeLifeMs)) // Время окончания токена
                .signWith(key)
                .compact();
    }

    // Получение claims из токена
    private Claims getAllClaimsFromToken(String token) {
        // return Jwts.parser().setSigningKey(key).build().parseClaimsJws(token).getBody();
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
    }
    // Получение определенного claim из токена
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    // Получение логина из токена
    public String getUsernameFromToken(String token) {
        return getAllClaimsFromToken(token).getSubject();
    }

    // Получение времени окончания токена
    public Date getExpirationDateFromToken(String token) {
        return getAllClaimsFromToken(token).getExpiration();
    }

    // Проверка истёк ли срок действия токена
    public Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    // Проверка на валидный токен (login должен совпадать и token должен быть действительным)
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
