package com.codeCracker.userservice.util;

import com.codeCracker.userservice.constants.ApplicationTestConstants;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultJwtBuilder;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import static com.codeCracker.userservice.constants.ApplicationConstants.SECRET_KEY;
import static com.codeCracker.userservice.constants.ApplicationTestConstants.SAMPLE_UUID;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class JwtRequestFilterTest {

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private JwtRequestFilter jwtRequestFilter;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain chain;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testDoFilterInternalValidToken() throws ServletException, IOException {
        String jwt = new DefaultJwtBuilder().setSubject(SAMPLE_UUID).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
        String token = "Bearer " + jwt;
        String username = ApplicationTestConstants.SAMPLE_UUID;
        UserDetails userDetails = mock(UserDetails.class);

        when(request.getHeader("Authorization")).thenReturn(token);
        when(jwtUtil.extractUsername(anyString())).thenReturn(username);
        when(userDetailsService.loadUserByUsername(anyString())).thenReturn(userDetails);
        when(jwtUtil.validateToken(anyString(), eq(userDetails))).thenReturn(true);
        when(userDetails.getAuthorities()).thenReturn(new ArrayList<>());

        jwtRequestFilter.doFilterInternal(request, response, chain);

        verify(userDetailsService).loadUserByUsername(username);
        verify(jwtUtil).validateToken(anyString(), eq(userDetails));
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        verify(chain).doFilter(request, response);
    }

    @Test
    public void testDoFilterInternalInvalidToken() throws ServletException, IOException {
        String token = "Bearer " + ApplicationTestConstants.SAMPLE_JWT;
        String username = ApplicationTestConstants.SAMPLE_UUID;
        UserDetails userDetails = mock(UserDetails.class);

        when(request.getHeader("Authorization")).thenReturn(token);
        when(jwtUtil.extractUsername(anyString())).thenReturn(username);
        when(userDetailsService.loadUserByUsername(anyString())).thenReturn(userDetails);
        when(jwtUtil.validateToken(anyString(), eq(userDetails))).thenReturn(false);

        jwtRequestFilter.doFilterInternal(request, response, chain);

        verify(userDetailsService).loadUserByUsername(username);
        verify(jwtUtil).validateToken(anyString(), eq(userDetails));
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(chain).doFilter(request, response);
    }

    @Test
    public void testDoFilterInternalNoToken() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn(null);

        jwtRequestFilter.doFilterInternal(request, response, chain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(chain).doFilter(request, response);
    }
}
