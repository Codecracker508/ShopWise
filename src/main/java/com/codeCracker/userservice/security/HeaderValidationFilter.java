package com.codeCracker.userservice.security;

import com.codeCracker.userservice.constants.ApplicationConstants;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.codeCracker.userservice.constants.ApplicationConstants.UNAUTHORIZED_ACCESS;

@SuppressWarnings("ALL")
@Component
public class HeaderValidationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String headerValue = request.getHeader(ApplicationConstants.Headers.INTERNAL_REQUEST);
        if (!"true".equals(headerValue)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write(UNAUTHORIZED_ACCESS);
            return;
        }
        filterChain.doFilter(request, response);
    }
}
