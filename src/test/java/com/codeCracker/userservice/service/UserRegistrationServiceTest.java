package com.codeCracker.userservice.service;

import com.codeCracker.userservice.constants.ApplicationTestConstants;
import com.codeCracker.userservice.converter.UserConverter;
import com.codeCracker.userservice.dto.model.MobileNumber;
import com.codeCracker.userservice.dto.model.OTPResponse;
import com.codeCracker.userservice.dto.model.UserDetailsDto;
import com.codeCracker.userservice.dto.request.CreateUser;
import com.codeCracker.userservice.dto.request.VerifyUser;
import com.codeCracker.userservice.dto.response.UserRegistration;
import com.codeCracker.userservice.dto.response.UserVerification;
import com.codeCracker.userservice.entity.UserTb;
import com.codeCracker.userservice.exceptions.InvalidOtpException;
import com.codeCracker.userservice.exceptions.UserNotFoundException;
import com.codeCracker.userservice.repo.UserRepository;
import com.codeCracker.userservice.service.impl.UserRegistrationServiceImpl;
import com.codeCracker.userservice.util.JwtUtil;
import com.codeCracker.userservice.util.OTPGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

import static com.codeCracker.userservice.constants.ApplicationConstants.OTP_ERROR;
import static com.codeCracker.userservice.constants.ApplicationConstants.OTP_SUCCESS;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserRegistrationServiceTest {

    @Mock
    private UserConverter userConverter;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private OTPGenerator otpGenerator;

    @Mock
    private UserAuthService userAuthService;

    @InjectMocks
    private UserRegistrationServiceImpl userRegistrationService;
    
    String otp = new Random().ints(100000,999999).toString();
    MobileNumber mobileNumber = new MobileNumber("+91", "1234567890");

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testUserRegistration() {
        CreateUser createUser = new CreateUser();
        createUser.setMobile(mobileNumber);

        UserTb userTb = new UserTb();
        userTb.setUserId(UUID.randomUUID());

        OTPResponse otpResponse = new OTPResponse();
        otpResponse.setOtp(otp);
        otpResponse.setOtpMessage("OTP sent successfully");

        when(otpGenerator.generateOtp(anyString())).thenReturn(otpResponse);
        when(userConverter.convertUserToEntity(any(CreateUser.class))).thenReturn(userTb);
        when(userRepository.save(any(UserTb.class))).thenReturn(userTb);

        UserRegistration userRegistration = userRegistrationService.userRegistration(createUser);

        assertEquals(otp, userRegistration.getOtp());
        assertEquals("OTP sent successfully", userRegistration.getMessage());
        assertNotNull(userRegistration.getUserId());
    }

    @Test
    public void testUserRegistrationOtpError() {
        CreateUser createUser = new CreateUser();
        createUser.setMobile(mobileNumber);

        when(otpGenerator.generateOtp(anyString())).thenThrow(new RuntimeException("OTP generation error"));

        UserRegistration userRegistration = userRegistrationService.userRegistration(createUser);

        assertNull(userRegistration.getUserId());
        assertEquals(OTP_ERROR, userRegistration.getMessage());
    }

    @Test
    public void testUserVerification() throws Exception {
        VerifyUser verifyUser = new VerifyUser();
        verifyUser.setUserId(ApplicationTestConstants.SAMPLE_UUID);
        verifyUser.setOtp(otp);
        verifyUser.setMobile(mobileNumber);

        UserTb userTb = new UserTb();
        userTb.setUserId(UUID.randomUUID());

        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(userTb));
        when(otpGenerator.getCacheOtp(anyString())).thenReturn(otp);
        when(userAuthService.loadUserByUsername(anyString())).thenReturn(new User(ApplicationTestConstants.SAMPLE_UUID, "password", new ArrayList<>()));
        when(jwtUtil.generateToken(any(UserDetails.class))).thenReturn("jwt-token");

        UserVerification userVerification = userRegistrationService.userVerification(verifyUser);

        assertEquals("jwt-token", userVerification.getAccessToken());
        assertEquals(OTP_SUCCESS, userVerification.getMessage());
    }

    @Test
    public void testUserVerificationInvalidOtp() {
        VerifyUser verifyUser = new VerifyUser();
        verifyUser.setUserId(ApplicationTestConstants.SAMPLE_UUID);
        verifyUser.setOtp(otp);
        verifyUser.setMobile(mobileNumber);

        UserTb userTb = new UserTb();
        userTb.setUserId(UUID.randomUUID());

        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(userTb));
        when(otpGenerator.getCacheOtp(anyString())).thenReturn("654321");

        assertThrows(InvalidOtpException.class, () -> {
            userRegistrationService.userVerification(verifyUser);
        });
    }

    @Test
    public void testUserVerificationUserNotFound() {
        VerifyUser verifyUser = new VerifyUser();
        verifyUser.setUserId(ApplicationTestConstants.SAMPLE_UUID);
        verifyUser.setOtp(otp);
        verifyUser.setMobile(new MobileNumber("+91", "1234567890"));

        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            userRegistrationService.userVerification(verifyUser);
        });
    }

    @Test
    public void testGetAllUsers() {
        List<UserTb> users = new ArrayList<>();
        users.add(new UserTb());

        when(userRepository.findAll()).thenReturn(users);
        when(userConverter.userToUserDetails(any(UserTb.class))).thenReturn(new UserDetailsDto());

        List<UserDetailsDto> userDetailsDtos = userRegistrationService.getAllUsers();

        assertEquals(1, userDetailsDtos.size());
    }
}
