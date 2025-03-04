package com.codeCracker.userservice.rest;

import com.codeCracker.userservice.constants.ApplicationConstants;
import com.codeCracker.userservice.dto.model.MobileNumber;
import com.codeCracker.userservice.dto.model.Name;
import com.codeCracker.userservice.dto.model.UserDetailsDto;
import com.codeCracker.userservice.dto.request.CreateUser;
import com.codeCracker.userservice.dto.request.VerifyUser;
import com.codeCracker.userservice.dto.response.UserRegistration;
import com.codeCracker.userservice.dto.response.UserUpdateResponse;
import com.codeCracker.userservice.dto.response.UserVerification;
import com.codeCracker.userservice.exceptions.UserNotFoundException;
import com.codeCracker.userservice.exceptions.UserNotVerifiedException;
import com.codeCracker.userservice.globalExceptions.GlobalExceptionHandler;
import com.codeCracker.userservice.service.UserRegistrationService;
import com.codeCracker.userservice.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultJwtBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Date;
import java.util.List;
import java.util.Random;

import static com.codeCracker.userservice.constants.ApplicationConstants.Headers.INTERNAL_REQUEST;
import static com.codeCracker.userservice.constants.ApplicationConstants.*;
import static com.codeCracker.userservice.constants.ApplicationTestConstants.SAMPLE_JWT;
import static com.codeCracker.userservice.constants.ApplicationTestConstants.SAMPLE_UUID;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserLoginController.class)
@ContextConfiguration(classes = {UserLoginController.class, GlobalExceptionHandler.class})
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
    private JwtUtil jwtUtil;
    @InjectMocks
    private UserLoginController userLoginController;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);

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
        mockMvc = MockMvcBuilders.standaloneSetup(userLoginController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void testUserRegistration() throws Exception {
        when(userRegistrationService.userRegistration(createUser)).thenReturn(userRegistration);

        this.mockMvc.perform(post(ApplicationConstants.URLS.NEW_USER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(createUser)))

                .andExpect(status().isCreated());
    }

    @Test
    void testUserRegistration_InvalidRequest() throws Exception {
        CreateUser invalidUser = new CreateUser(null, mobileNumber); // Invalid because name is null

        this.mockMvc.perform(post(ApplicationConstants.URLS.NEW_USER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(invalidUser)))

                .andExpect(status().isCreated());
    }

    @Test
    void testUserVerification() throws Exception {
        when(userRegistrationService.userVerification(verifyUser)).thenReturn(userVerification);

        this.mockMvc.perform(post(ApplicationConstants.URLS.VERIFY_USER)
                        .header(INTERNAL_REQUEST, true)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(verifyUser)))
                .andExpect(status().isOk());
    }

    @Test
    void testUserVerification_InvalidOTP() throws Exception {
        VerifyUser invalidVerifyUser = VerifyUser.builder()
                .userId(SAMPLE_UUID)
                .otp("123") // Invalid because OTP is too short
                .mobile(mobileNumber).build();
        this.mockMvc.perform(post(ApplicationConstants.URLS.VERIFY_USER)
                .header(INTERNAL_REQUEST, true)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(invalidVerifyUser))).andExpect(status().isOk());
    }

    @Test
    void testGetAllUsers() throws Exception {
        String jwt = new DefaultJwtBuilder().setSubject(SAMPLE_UUID).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();

        when(userRegistrationService.getAllUsers()).thenReturn(List.of(userDetailsDto));
        this.mockMvc.perform(get(ApplicationConstants.URLS.ALL_USERS)
                        .header(INTERNAL_REQUEST, true)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(ApplicationConstants.Headers.AUTHORIZATION, jwt))
                .andExpect(status().isOk());
    }

    @Test
    void testGetUser() throws Exception {
        String jwt = SAMPLE_JWT;

        when(userRegistrationService.getUser(jwt)).thenReturn(userDetailsDto);

        this.mockMvc.perform(get(ApplicationConstants.URLS.GET_USER)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(ApplicationConstants.Headers.AUTHORIZATION, jwt))
                .andExpect(status().isOk());
    }

    @Test
    void testGetUser_NotFound() throws Exception {
        String jwt = SAMPLE_JWT;

        when(userRegistrationService.getUser(jwt)).thenThrow(new UserNotFoundException(SAMPLE_UUID));

        this.mockMvc.perform(get(ApplicationConstants.URLS.GET_USER)
                .accept(MediaType.APPLICATION_JSON)
                .header(ApplicationConstants.Headers.AUTHORIZATION, jwt)).andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateUser() throws Exception {
        when(userRegistrationService.updateUser(userDetailsDto)).thenReturn(new UserUpdateResponse(userDetailsDto, UPDATED_USER));

        this.mockMvc.perform(post(ApplicationConstants.URLS.UPDATE_USER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(userDetailsDto)))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdateUser_NotVerified() throws Exception {
        when(userRegistrationService.updateUser(userDetailsDto)).thenThrow(new UserNotVerifiedException());

        this.mockMvc.perform(post(ApplicationConstants.URLS.UPDATE_USER)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(userDetailsDto))).andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateUser_NotFound() throws Exception {
        when(userRegistrationService.updateUser(userDetailsDto)).thenThrow(new UserNotFoundException(SAMPLE_UUID));

        this.mockMvc.perform(post(ApplicationConstants.URLS.UPDATE_USER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(userDetailsDto)))
                .andExpect(status().isBadRequest());
    }
}
