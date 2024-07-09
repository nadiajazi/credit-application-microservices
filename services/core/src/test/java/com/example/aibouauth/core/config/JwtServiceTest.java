package com.example.aibouauth.core.config;

import static org.junit.jupiter.api.Assertions.*;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;

import java.lang.reflect.Field;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class JwtServiceTest {

    @Mock
    private UserDetails userDetails;

    @Mock
    private Key key;

    @InjectMocks
    private JwtService jwtService;

    private final String secretKey = "mysecretkey";

    @BeforeEach
    public void setup() throws NoSuchFieldException, IllegalAccessException {
        MockitoAnnotations.openMocks(this);
        jwtService = new JwtService();

        // Set private fields via reflection
        Field jwtExpirationField = JwtService.class.getDeclaredField("jwtExpiration");
        jwtExpirationField.setAccessible(true);
        // 1 hour
        long jwtExpiration = 3600000;
        jwtExpirationField.set(jwtService, jwtExpiration);

        Field secretKeyField = JwtService.class.getDeclaredField("secretKey");
        secretKeyField.setAccessible(true);
        secretKeyField.set(jwtService, secretKey);

        Field refreshExpirationField = JwtService.class.getDeclaredField("refreshExpiration");
        refreshExpirationField.setAccessible(true);
        // 1 day
        long refreshExpiration = 86400000;
        refreshExpirationField.set(jwtService, refreshExpiration);
    }
    @Test
    public void testExtractUsername() {
        String token = jwtService.generateToken(new HashMap<>(), userDetails);
        String username = jwtService.extractUsername(token);
        assertEquals(userDetails.getUsername(), username);
    }

    @Test
    public void testGenerateToken() {
        Map<String, Object> extraClaims = new HashMap<>();
        String token = jwtService.generateToken(extraClaims, userDetails);

        assertNotNull(token);
        Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes())).build().parseClaimsJws(token);
        assertEquals(userDetails.getUsername(), claims.getBody().getSubject());
    }

    @Test
    public void testGenerateRefreshToken() {
        String refreshToken = jwtService.generateRefreshToken(userDetails);

        assertNotNull(refreshToken);
        Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes())).build().parseClaimsJws(refreshToken);
        assertEquals(userDetails.getUsername(), claims.getBody().getSubject());
    }

    @Test
    public void testIsTokenValid() {
        String token = jwtService.generateToken(new HashMap<>(), userDetails);
        boolean isValid = jwtService.isTokenValid(token, userDetails);

        assertTrue(isValid);
    }

    @Test
    public void testIsTokenExpired() {
        String expiredToken = Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis() - 1000)) // Set issued date in the past
                .setExpiration(new Date(System.currentTimeMillis() - 500)) // Set expiration date in the past
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()), SignatureAlgorithm.HS256)
                .compact();

        assertTrue(jwtService.isTokenExpired(expiredToken));
    }

    @Test
    public void testExtractExpiration() {
        String token = jwtService.generateToken(new HashMap<>(), userDetails);
        Date expiration = jwtService.extractExpiration(token);

        assertNotNull(expiration);
    }

    @Test
    public void testExtractAllClaims() {
        String token = jwtService.generateToken(new HashMap<>(), userDetails);
        Claims claims = jwtService.extractAllClaims(token);

        assertNotNull(claims);
        assertEquals(userDetails.getUsername(), claims.getSubject());
    }
}
