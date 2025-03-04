package com.codeCracker.userservice.util;

import com.codeCracker.userservice.dto.model.MobileNumber;
import com.codeCracker.userservice.dto.model.Name;
import com.codeCracker.userservice.dto.model.UserDetailsDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultJwtBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Date;

import static com.codeCracker.userservice.constants.ApplicationConstants.SECRET_KEY;
import static com.codeCracker.userservice.constants.ApplicationTestConstants.SAMPLE_UUID;
import static org.junit.jupiter.api.Assertions.*;

public class JwtUtilTest {

    @InjectMocks
    private JwtUtil jwtUtil;

    @Mock
    private UserDetailsService userDetailsService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testExtractUsername() {
        String username = SAMPLE_UUID;
        String token = jwtUtil.generateToken(new UserDetailsDto(username,
                new Name("john", "abraham", "gates"),
                new MobileNumber("+91", "1234567890")));

        String extractedUsername = jwtUtil.extractUsername(token);
        assertEquals(username, extractedUsername);
    }

    @Test
    public void testGenerateToken() {
        UserDetailsDto userDetails = new UserDetailsDto(SAMPLE_UUID,
                new Name("john", "abraham", "gates"),
                new MobileNumber("+91", "1234567890"));

        String token = jwtUtil.generateToken(userDetails);

        assertNotNull(token);
    }

    @Test
    public void testValidateToken() {
        UserDetailsDto userDetails = new UserDetailsDto(SAMPLE_UUID,
                new Name("john", "abraham", "gates"),
                new MobileNumber("+91", "1234567890"));
        String token = jwtUtil.generateToken(userDetails);

        assertTrue(jwtUtil.validateToken(token, userDetails));
    }

    @Test
    public void testValidateTokenExpired() {
        String jwt = new DefaultJwtBuilder().setSubject(SAMPLE_UUID).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() - 10))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();

        UserDetailsDto userDetails = new UserDetailsDto(SAMPLE_UUID,
                new Name("john", "abraham", "gates"),
                new MobileNumber("+91", "1234567890"));
        ExpiredJwtException exception = Assertions.assertThrows(
                ExpiredJwtException.class, () -> jwtUtil.validateToken(jwt, userDetails)
        );
        assertNotNull(exception);
    }

    @Test
    public void testExtractAllClaims() {
        UserDetailsDto userDetails = new UserDetailsDto(SAMPLE_UUID,
                new Name("john", "abraham", "gates"),
                new MobileNumber("+91", "1234567890"));
        String token = jwtUtil.generateToken(userDetails);

        Claims claims = jwtUtil.extractAllClaims(token);

        assertEquals(SAMPLE_UUID, claims.getSubject());
    }
}
