package com.kd0s.user_auth_service.config.auth;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;

@Service
public class TokenProvider {
    @Value("${security.jwt.token.secret-key}")
    private String JWT_SECRET;

    public String generateAccessToken(String username) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(JWT_SECRET);
            return JWT.create()
                    .withSubject(username)
                    .withClaim("username", username)
                    .withExpiresAt(genAccessExpirationDate(0, 5))
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new JWTCreationException("Error while creating Access token", exception);
        }
    }

    public String generateRefreshToken(String username) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(JWT_SECRET);
            return JWT.create()
                    .withSubject(username)
                    .withClaim("username", username)
                    .withExpiresAt(genAccessExpirationDate(3, 0))
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new JWTCreationException("Error while creating Refresh token", exception);
        }
    }

    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(JWT_SECRET);
            return JWT.require(algorithm)
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException exception) {
            throw new JWTVerificationException("Error while validating token", exception);
        }
    }

    private Instant genAccessExpirationDate(int days, int mins) {
        return LocalDateTime.now().plusDays(days).plusMinutes(mins).toInstant(ZoneOffset.UTC);
    }
}
