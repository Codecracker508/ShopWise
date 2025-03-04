package com.codeCracker.userservice.service;

import com.codeCracker.userservice.converter.UserConverter;
import com.codeCracker.userservice.dto.model.MobileNumber;
import com.codeCracker.userservice.dto.model.Name;
import com.codeCracker.userservice.dto.model.OTPResponse;
import com.codeCracker.userservice.dto.model.UserDetailsDto;
import com.codeCracker.userservice.dto.request.CreateUser;
import com.codeCracker.userservice.dto.request.VerifyUser;
import com.codeCracker.userservice.dto.response.UserRegistration;
import com.codeCracker.userservice.dto.response.UserUpdateResponse;
import com.codeCracker.userservice.dto.response.UserVerification;
import com.codeCracker.userservice.entity.UserTb;
import com.codeCracker.userservice.exceptions.InvalidOtpException;
import com.codeCracker.userservice.exceptions.UserNotFoundException;
import com.codeCracker.userservice.exceptions.UserNotVerifiedException;
import com.codeCracker.userservice.repo.UserRepository;
import com.codeCracker.userservice.service.impl.UserRegistrationServiceImpl;
import com.codeCracker.userservice.util.JwtUtil;
import com.codeCracker.userservice.util.OTPGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.codeCracker.userservice.constants.ApplicationConstants.*;
import static com.codeCracker.userservice.constants.ApplicationTestConstants.SAMPLE_UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserRegistrationServiceTest {

    @Mock
    private UserConverter userConverter;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private OTPGenerator otpGenerator;


    @InjectMocks
    private UserRegistrationServiceImpl userRegistrationService;

    private UUID sampleUUID;
    private UserTb userTb;
    private UserDetailsDto userDetailsDto;
    private CreateUser createUser;
    private VerifyUser verifyUser;
    private OTPResponse otpResponse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        Name name = new Name("John", "M", "Doe");
        MobileNumber mobileNumber = new MobileNumber("+1", "1234567890");
        sampleUUID = UUID.fromString(SAMPLE_UUID);
        userTb = new UserTb(sampleUUID, name, mobileNumber, true);
        userTb.setUserId(sampleUUID);

        createUser = new CreateUser(name, mobileNumber);

        verifyUser = VerifyUser.builder()
                .userId(sampleUUID.toString())
                .otp("123456")
                .mobile(mobileNumber)
                .build();

        otpResponse = OTPResponse.builder()
                .otp("123456")
                .otpMessage(OTP_SUCCESS)
                .build();

        userDetailsDto = new UserDetailsDto(sampleUUID.toString(), name, mobileNumber);
    }

    @Test
    void testUserRegistration_Success() {
        when(otpGenerator.generateOtp(anyString())).thenReturn(otpResponse);
        when(userConverter.convertUserToEntity(createUser)).thenReturn(userTb);
        when(userRepository.save(any(UserTb.class))).thenReturn(userTb);

        UserRegistration userRegistration = userRegistrationService.userRegistration(createUser);

        assertNotNull(userRegistration);
        assertEquals(otpResponse.getOtp(), userRegistration.getOtp());
        assertEquals(otpResponse.getOtpMessage(), userRegistration.getMessage());
        assertEquals(sampleUUID.toString(), userRegistration.getUserId());
    }

    @Test
    void testUserRegistration_OtpGenerationError() {
        when(otpGenerator.generateOtp(anyString())).thenThrow(new RuntimeException("OTP generation error"));

        UserRegistration userRegistration = userRegistrationService.userRegistration(createUser);

        assertNotNull(userRegistration);
        assertNull(userRegistration.getUserId());
        assertEquals(OTP_ERROR, userRegistration.getMessage());
    }

    @Test
    void testUserVerification_Success() throws Exception {
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(userTb));
        when(otpGenerator.getCacheOtp(anyString())).thenReturn("123456");
        doNothing().when(otpGenerator).clearOtp(anyString());
        when(jwtUtil.generateToken(any(UserDetailsDto.class))).thenReturn("token");

        UserVerification userVerification = userRegistrationService.userVerification(verifyUser);

        assertNotNull(userVerification);
        assertEquals(OTP_SUCCESS, userVerification.getMessage());
        assertEquals("token", userVerification.getAccessToken());
    }

    @Test
    void testUserVerification_InvalidOtp() throws Exception {
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(userTb));
        when(otpGenerator.getCacheOtp(anyString())).thenReturn("654321");

        assertThrows(InvalidOtpException.class, () -> {
            userRegistrationService.userVerification(verifyUser);
        });
    }

    @Test
    void testUserVerification_UserNotFound() {
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            userRegistrationService.userVerification(verifyUser);
        });
    }

    @Test
    void testGetAllUsers() {
        when(userRepository.findAll()).thenReturn(List.of(userTb));
        when(userConverter.userToUserDetails(any(UserTb.class))).thenReturn(userDetailsDto);

        List<UserDetailsDto> allUsers = userRegistrationService.getAllUsers();

        assertNotNull(allUsers);
        assertEquals(1, allUsers.size());
        assertEquals(userDetailsDto, allUsers.get(0));
    }

    @Test
    void testGetUser_Success() throws UserNotFoundException {
        when(jwtUtil.extractUsername(anyString())).thenReturn(sampleUUID.toString());
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(userTb));
        when(userConverter.userToUserDetails(any(UserTb.class))).thenReturn(userDetailsDto);

        UserDetailsDto user = userRegistrationService.getUser("Bearer token");

        assertNotNull(user);
        assertEquals(userDetailsDto, user);
    }

    @Test
    void testGetUser_UserNotFound() {
        when(jwtUtil.extractUsername(anyString())).thenReturn(sampleUUID.toString());
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            userRegistrationService.getUser("Bearer token");
        });
    }

    @Test
    void testUpdateUser_Success() throws UserNotVerifiedException, UserNotFoundException {
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(userTb));
        when(userConverter.userDtoToUserTb(any(UserDetailsDto.class), any(UserTb.class))).thenReturn(userTb);


        UserUpdateResponse userUpdateResponse = userRegistrationService.updateUser(userDetailsDto);

        assertNotNull(userUpdateResponse);
        assertEquals(userDetailsDto, userUpdateResponse.getUserDetails());
        assertEquals(UPDATED_USER, userUpdateResponse.getMessage());
    }

    @Test
    void testUpdateUser_UserNotVerified() {
        UserTb userDetails = userTb;
        userDetails.setIsAuthenticated(false);
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(userTb));

        assertThrows(UserNotVerifiedException.class, () -> {
            userRegistrationService.updateUser(userDetailsDto);
        });
    }

    @Test
    void testUpdateUser_UserNotFound() {
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            userRegistrationService.updateUser(userDetailsDto);
        });
    }
}
