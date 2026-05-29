package com.lazysyntax.nutron.auth.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    /**
    * Clave de cifrado JWT genearado en PowerShell con Opensssl comando: openssl rand -base64 48
     * **/
    @Value("${jwt.secret}")
    private String secretKey;

    //@Value("${jwt.access-token-expiration:3600000}") // 1 hour
    @Value("${jwt.access-token-expiration:120000}") // 2min DEBUG
    private long accessTokenExpiration;

    @Value("${jwt.refresh-token-expiration:604800000}") // 7 days
    private long refreshTokenExpiration;

    /**
     * Genear un token de acceso con un tiempo de expiracion de 1h (2min para DEBUG)
     */

    public String generateAccessToken(String userId) {
        return buildToken(new HashMap<>(), userId, accessTokenExpiration);
    }

    /**
     * Genear un token de actualización con un tiempo de expiracion de 7 días
     */
    public String generateRefreshToken(String userId) {
        return buildToken(new HashMap<>(), userId, refreshTokenExpiration);
    }

    private String buildToken(Map<String, Object> extraClaims, String subject, long expiration) {
        return Jwts.builder()
                .claims(extraClaims)
                .subject(subject)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey())
                .compact();
    }

   /*
    * Los Calim son informacion útil definida en el token (Payload)
    *  que permite identificar al usuario y validar sus tokens (id, tiempo de expiracion, rol,...).
    */
    public String extractUserId(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSignInKey() {

        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public boolean isTokenValid(String token) {return !isTokenExpired(token);}

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
}
