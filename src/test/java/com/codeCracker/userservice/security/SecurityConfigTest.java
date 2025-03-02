package com.codeCracker.userservice.security;

import com.codeCracker.userservice.components.CustomAuthenticationEntryPoint;
import com.codeCracker.userservice.constants.ApplicationConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static com.codeCracker.userservice.constants.ApplicationConstants.Headers.INTERNAL_REQUEST;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private HeaderValidationFilter headerValidationFilter;

    @Mock
    private CustomAuthenticationEntryPoint authenticationEntryPoint;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testWhitelistedUrls() throws Exception {
        mockMvc.perform(get("/swagger-ui/index.html")
                        .header(INTERNAL_REQUEST,true))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    public void testAuthenticatedUrl() throws Exception {
        mockMvc.perform(get(ApplicationConstants.URLS.ALL_USERS).header(INTERNAL_REQUEST,true))
                .andExpect(status().isOk());
    }

    @Test
    public void testNonAuthenticatedUrl() throws Exception {
        mockMvc.perform(get(ApplicationConstants.URLS.ALL_USERS))
                .andExpect(status().isUnauthorized());
    }
}
