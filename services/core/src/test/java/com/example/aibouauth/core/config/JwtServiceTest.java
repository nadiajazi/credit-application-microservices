package com.example.aibouauth.core.config;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;

import javax.crypto.SecretKey;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Base64;

public class JwtServiceTest {

    @Mock
    private UserDetails userDetails;

    @InjectMocks
    private JwtService jwtService;

    private String secretKey;

    @BeforeEach
    public void setup() throws NoSuchFieldException, IllegalAccessException {
        MockitoAnnotations.openMocks(this);
        jwtService = new JwtService();

        SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        secretKey = Base64.getEncoder().encodeToString(key.getEncoded());


        Field jwtExpirationField = JwtService.class.getDeclaredField("jwtExpiration");
        jwtExpirationField.setAccessible(true);
        jwtExpirationField.set(jwtService, 3600000L);

        Field secretKeyField = JwtService.class.getDeclaredField("secretKey");
        secretKeyField.setAccessible(true);
        secretKeyField.set(jwtService, secretKey);

        Field refreshExpirationField = JwtService.class.getDeclaredField("refreshExpiration");
        refreshExpirationField.setAccessible(true);
        refreshExpirationField.set(jwtService, 86400000L);
    }

    @Test
    public void testExtractUsername() {
        when(userDetails.getUsername()).thenReturn("testuser");
        String token = jwtService.generateToken(new HashMap<>(), userDetails);
        String username = jwtService.extractUsername(token);
        assertEquals(userDetails.getUsername(), username);
    }

    @Test
    public void testGenerateToken() {
        when(userDetails.getUsername()).thenReturn("testuser");
        Map<String, Object> extraClaims = new HashMap<>();
        String token = jwtService.generateToken(extraClaims, userDetails);

        assertNotNull(token);
        Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(Keys.hmacShaKeyFor(Base64.getDecoder().decode(secretKey))).build().parseClaimsJws(token);
        assertEquals(userDetails.getUsername(), claims.getBody().getSubject());
    }

    @Test
    public void testGenerateRefreshToken() {
        when(userDetails.getUsername()).thenReturn("testuser");
        String refreshToken = jwtService.generateRefreshToken(userDetails);

        assertNotNull(refreshToken);
        Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(Keys.hmacShaKeyFor(Base64.getDecoder().decode(secretKey))).build().parseClaimsJws(refreshToken);
        assertEquals(userDetails.getUsername(), claims.getBody().getSubject());
    }

    @Test
    public void testIsTokenValid() {
        when(userDetails.getUsername()).thenReturn("testuser");
        String token = jwtService.generateToken(new HashMap<>(), userDetails);
        boolean isValid = jwtService.isTokenValid(token, userDetails);

        assertTrue(isValid);
    }

    @Test
    public void testIsTokenExpired() {
        when(userDetails.getUsername()).thenReturn("testuser");

        long currentTimeMillis = System.currentTimeMillis();
        Date issuedAt = new Date(currentTimeMillis + 1000);
        Date expiration = new Date(currentTimeMillis + 5000);

        String token = Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(issuedAt)
                .setExpiration(expiration)
                .signWith(Keys.hmacShaKeyFor(Base64.getDecoder().decode(secretKey)), SignatureAlgorithm.HS256)
                .compact();


        assertFalse(jwtService.isTokenExpired(token));}
    @Test
    public void testExtractExpiration() {
        when(userDetails.getUsername()).thenReturn("testuser");
        String token = jwtService.generateToken(new HashMap<>(), userDetails);
        Date expiration = jwtService.extractExpiration(token);

        assertNotNull(expiration);
    }

    @Test
    public void testExtractAllClaims() {
        when(userDetails.getUsername()).thenReturn("testuser");
        String token = jwtService.generateToken(new HashMap<>(), userDetails);
        Claims claims = jwtService.extractAllClaims(token);

        assertNotNull(claims);
        assertEquals(userDetails.getUsername(), claims.getSubject());
    }
}
