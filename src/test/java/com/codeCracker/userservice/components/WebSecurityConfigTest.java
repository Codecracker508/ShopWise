package com.codeCracker.userservice.components;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
class WebSecurityConfigTest {

    @Autowired
    private AuthenticationConfiguration authenticationConfiguration;

    @Autowired
    private WebSecurityConfig webSecurityConfig;

    @Test
    void authenticationManager() throws Exception {
        authenticationConfiguration = Mockito.mock(AuthenticationConfiguration.class);
        webSecurityConfig = Mockito.mock(WebSecurityConfig.class);

        AuthenticationManager authenticationManagerClass = Mockito.mock(AuthenticationManager.class);

        Mockito.when(webSecurityConfig.authenticationManager(any())).thenReturn(authenticationManagerClass);

        AuthenticationManager authenticationManager = webSecurityConfig.authenticationManager(authenticationConfiguration);
        assertThat(authenticationManager).isEqualTo(authenticationManagerClass);
    }
}