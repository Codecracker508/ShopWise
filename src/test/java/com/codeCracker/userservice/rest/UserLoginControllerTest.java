package com.codeCracker.userservice.rest;

import com.codeCracker.userservice.components.CustomAuthenticationEntryPoint;
import com.codeCracker.userservice.constants.ApplicationConstants;
import com.codeCracker.userservice.dto.model.MobileNumber;
import com.codeCracker.userservice.dto.model.Name;
import com.codeCracker.userservice.dto.model.UserDetailsDto;
import com.codeCracker.userservice.dto.request.CreateUser;
import com.codeCracker.userservice.dto.request.VerifyUser;
import com.codeCracker.userservice.dto.response.UserRegistration;
import com.codeCracker.userservice.dto.response.UserVerification;
import com.codeCracker.userservice.security.SecurityConfig;
import com.codeCracker.userservice.service.UserAuthService;
import com.codeCracker.userservice.service.UserRegistrationService;
import com.codeCracker.userservice.util.JwtRequestFilter;
import com.codeCracker.userservice.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultJwtBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;
import java.util.List;
import java.util.Random;

import static com.codeCracker.userservice.constants.ApplicationConstants.*;
import static com.codeCracker.userservice.constants.ApplicationTestConstants.SAMPLE_JWT;
import static com.codeCracker.userservice.constants.ApplicationTestConstants.SAMPLE_UUID;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserLoginController.class)
@ContextConfiguration(classes = {UserLoginController.class, SecurityConfig.class, CustomAuthenticationEntryPoint.class})
class UserLoginControllerTest {

    VerifyUser verifyUser;
    CreateUser createUser;
    MobileNumber mobileNumber;
    Name name;
    UserDetailsDto userDetailsDto;
    UserVerification userVerification;
    UserRegistration userRegistration;
    AutoCloseable autoCloseable;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserRegistrationService userRegistrationService;
    @MockBean
    private UserAuthService userAuthService;
    @MockBean
    private JwtUtil jwtUtil;
    @MockBean
    private JwtRequestFilter jwtRequestFilter;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);

        // Initialize the real ObjectMapper and ObjectWriter
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter objectWriter = objectMapper.writer().withDefaultPrettyPrinter();

        String otp = String.valueOf(new Random().nextInt(899999) + 100000); // Generate a 6-digit OTP
        name = new Name("John", "M", "Gates");
        mobileNumber = new MobileNumber("+91", "1234567890");
        verifyUser = VerifyUser.builder()
                .userId(SAMPLE_UUID)
                .otp(otp)
                .mobile(mobileNumber).build();
        createUser = new CreateUser(name, mobileNumber);
        userDetailsDto = new UserDetailsDto(SAMPLE_UUID, name, mobileNumber);
        userVerification = UserVerification.builder()
                .message(OTP_SUCCESS)
                .accessToken(SAMPLE_JWT)
                .build();
        userRegistration = UserRegistration.builder()
                .userId(SAMPLE_UUID)
                .otp(otp)
                .message(WELCOME_USER)
                .build();
    }

    @Test
    void testUserRegistration() throws Exception {
        when(userRegistrationService.userRegistration(createUser)).thenReturn(userRegistration);

        this.mockMvc.perform(post(ApplicationConstants.URLS.NEW_USER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.valueOf(createUser)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void testUserVerification() throws Exception {
        when(userRegistrationService.userVerification(verifyUser)).thenReturn(userVerification);

        this.mockMvc.perform(post(ApplicationConstants.URLS.VERIFY_USER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.valueOf(verifyUser)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void testGetAllUsers() throws Exception {
        String jwt = new DefaultJwtBuilder().setSubject(SAMPLE_UUID).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();

        when(userRegistrationService.getAllUsers()).thenReturn(List.of(userDetailsDto));
        this.mockMvc.perform(get(ApplicationConstants.URLS.ALL_USERS)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(ApplicationConstants.Headers.AUTHORIZATION, jwt))
                .andDo(print())
                .andExpect(status().isOk());
    }

}
