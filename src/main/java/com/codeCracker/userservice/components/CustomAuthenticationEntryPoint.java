package com.codeCracker.userservice.components;

import com.codeCracker.userservice.dto.model.ShopWiseDefaultError;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.codeCracker.userservice.constants.ErrorConstants.ErrorCodes.DEFAULT_ERROR_CODE;
import static com.codeCracker.userservice.constants.ErrorConstants.ReasonCodes.INVALID_INPUT_VALUE;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(String.valueOf(ShopWiseDefaultError.builder()
                .ErrorCode(DEFAULT_ERROR_CODE)
                .Description(authException.getMessage())
                .ReasonCode(INVALID_INPUT_VALUE)
                .build()));
    }

}
