package com.codeCracker.userservice.interceptor;

import com.codeCracker.userservice.constants.ErrorConstants;
import com.codeCracker.userservice.dto.model.ShopWiseDefaultError;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.codeCracker.userservice.constants.ApplicationConstants.Headers.INTERNAL_REQUEST;

@Component
@Slf4j
@SuppressWarnings("all")
public class RequestFilter extends OncePerRequestFilter {
    private final ShopWiseDefaultError defaultError = new ShopWiseDefaultError();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String internalRequestHeader = request.getHeader(INTERNAL_REQUEST);

        if (Boolean.TRUE.toString().equalsIgnoreCase(internalRequestHeader)) {
            log.debug("Internal request validated successfully");
            filterChain.doFilter(request, response);
        } else {
            log.warn("Unauthorized request received. Request URI: {}", request.getRequestURI());
            sendUnauthorizedResponse(response);
        }
    }

    private void sendUnauthorizedResponse(HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        defaultError.setErrorCode(ErrorConstants.ErrorCodes.UNAUTHORIZED);
        defaultError.setDescription("Unauthorized Request");
        defaultError.setSource("header");
        defaultError.setReasonCode(ErrorConstants.ReasonCodes.UNAUTHORIZED);

        response.getWriter().write(objectMapper.writeValueAsString(defaultError));
    }
}
