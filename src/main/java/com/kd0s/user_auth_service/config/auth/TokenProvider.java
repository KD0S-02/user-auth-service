package com.kd0s.user_auth_service.config.auth;

import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

@Service
public class TokenProvider {
    @Value("${security.jwt.token.private-key}")
    private String PRIVATE_KEY;

    @Value("${security.jwt.token.public-key}")
    private String PUBLIC_KEY;

    private RSAPrivateKey getPrivateKey() throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(PRIVATE_KEY);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        RSAPrivateKey privateKey = (RSAPrivateKey) keyFactory.generatePrivate(keySpec);

        return privateKey;
    }

    private RSAPublicKey getPublicKey() throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(PUBLIC_KEY);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        RSAPublicKey publicKey = (RSAPublicKey) keyFactory.generatePublic(keySpec);

        return publicKey;
    }

    public String generateAccessToken(String username) throws Exception {
        try {
            RSAPrivateKey privateKey = getPrivateKey();
            Algorithm algorithm = Algorithm.RSA256(privateKey);
            return JWT.create()
                    .withSubject(username)
                    .withIssuer("user-auth-service")
                    .withClaim("type", "access")
                    .withExpiresAt(genAccessExpirationDate(0, 20))
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new JWTCreationException(
                    "Error while creating Access token", exception);
        }
    }

    public String generateRefreshToken(String username) throws Exception {
        try {
            RSAPrivateKey privateKey = getPrivateKey();
            Algorithm algorithm = Algorithm.RSA256(privateKey);
            return JWT.create()
                    .withSubject(username)
                    .withIssuer("user-auth-service")
                    .withClaim("type", "refresh")
                    .withExpiresAt(genAccessExpirationDate(10, 0))
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new JWTCreationException(
                    "Error while creating Refresh token", exception);
        }
    }

    public String validateToken(String token) throws Exception {
        try {
            RSAPublicKey publicKey = getPublicKey();
            Algorithm algorithm = Algorithm.RSA256(publicKey);
            JWTVerifier verifier = JWT.require(algorithm).withIssuer("user-auth-service").build();
            DecodedJWT jwt = verifier.verify(token);
            return jwt.getSubject();
        } catch (JWTVerificationException exception) {
            throw new JWTVerificationException(
                    "Error while validating token", exception);
        }
    }

    private Instant genAccessExpirationDate(int days, int mins) {
        return LocalDateTime.now().plusDays(days).plusMinutes(mins).toInstant(ZoneOffset.UTC);
    }
}
