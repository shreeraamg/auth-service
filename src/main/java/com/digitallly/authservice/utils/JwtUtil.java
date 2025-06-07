package com.digitallly.authservice.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class JwtUtil {

    @Value("${jwt.expiry}")
    private long jwtExpiry;

    @Value("${jwt.secret}")
    private String jwtSecret;

    public String generateToken(UserDetails userDetails) {
        return JWT
                .create()
                .withSubject(userDetails.getUsername())
                .withIssuedAt(Instant.now())
                .withExpiresAt(Instant.now().plusSeconds(jwtExpiry))
                .sign(getSigningAlgorithm());
    }

    public String extractEmail(String token) {
        return getVerifier().verify(token).getSubject();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        try {
            DecodedJWT decodedJWT = getVerifier().verify(token);
            String username = decodedJWT.getSubject();
            return username.equals(userDetails.getUsername()) && !isTokenExpired(decodedJWT);
        } catch (JWTVerificationException e) {
            return false;
        }
    }

    private boolean isTokenExpired(DecodedJWT decodedJWT) {
        return decodedJWT.getExpiresAtAsInstant().isBefore(Instant.now());
    }

    private JWTVerifier getVerifier() {
        return JWT.require(getSigningAlgorithm()).build();
    }

    private Algorithm getSigningAlgorithm() {
        return Algorithm.HMAC256(jwtSecret);
    }

}
