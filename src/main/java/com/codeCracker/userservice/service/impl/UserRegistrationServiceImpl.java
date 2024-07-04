package com.codeCracker.userservice.service.impl;

import com.codeCracker.userservice.converter.UserConverter;
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
import com.codeCracker.userservice.service.UserAuthService;
import com.codeCracker.userservice.service.UserRegistrationService;
import com.codeCracker.userservice.util.JwtUtil;
import com.codeCracker.userservice.util.OTPGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.codeCracker.userservice.constants.ApplicationConstants.*;

@Slf4j
@Service
public class UserRegistrationServiceImpl implements UserRegistrationService {

    @Autowired
    private UserConverter userConverter;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private OTPGenerator otpGenerator;

    @Autowired
    private UserAuthService userAuthService;

    @Override
    public UserRegistration userRegistration(CreateUser createUser) {
        OTPResponse otpResponse;
        String mobileNumber = createUser.getMobile().getCountryCode() + createUser.getMobile().getPhoneNumber();
        try {
            log.info("Generating OTP using - {}", mobileNumber);
            otpResponse = otpGenerator.generateOtp(mobileNumber);
        } catch (Exception e) {
            log.error("Error while generating OTP - {}", e.getMessage());
            return UserRegistration.builder().userId(null).message(OTP_ERROR).build();
        }
        log.info("Creating user");
        UserTb newUser = userRepository.save(userConverter.convertUserToEntity(createUser));
        return UserRegistration.builder().otp(otpResponse.getOtp()).message(otpResponse.getOtpMessage()).userId(newUser.getUserId().toString()).build();
    }

    @Override
    public UserVerification userVerification(VerifyUser verifyUser) throws Exception {
        String phoneNumber = verifyUser.getMobile().getCountryCode() + verifyUser.getMobile().getPhoneNumber();
        if (isUserPresent(verifyUser.getUserId())) {
            if (verifyOtp(verifyUser.getOtp(), phoneNumber)) {
                return verifiedUser(verifyUser);
            } else {
                throw new InvalidOtpException();
            }
        } else {
            throw new UserNotFoundException(verifyUser.getUserId());
        }
    }

    @Override
    public List<UserDetailsDto> getAllUsers() {
        List<UserDetailsDto> userDetailsDto = new ArrayList<>();
        userRepository.findAll().forEach(userTb -> userDetailsDto.add(userConverter.userToUserDetails(userTb)));
        return userDetailsDto;
    }

    @Override
    public UserDetailsDto getUser(String authorisation) throws UserNotFoundException {
        UUID userId = getUserId(authorisation);
        Optional<UserTb> userDetails = userRepository.findById(userId);
        if (userDetails.isPresent()) {
            return userConverter.userToUserDetails(userDetails.get());
        } else {
            throw new UserNotFoundException(userId.toString());
        }
    }

    @Override
    public UserUpdateResponse updateUser(UserDetailsDto userDetailsDto) throws UserNotVerifiedException, UserNotFoundException {
        Optional<UserTb> getUser = userRepository.findById(UUID.fromString(userDetailsDto.getUserId()));
        if (isUserPresent(userDetailsDto.getUserId()) && getUser.isPresent()) {
            if (isUserAuthenticated(userDetailsDto.getUserId())) {
                UserTb userDetails = userConverter.userDtoToUserTb(userDetailsDto, getUser.get());
                userRepository.updateUserByUserId(UUID.fromString(userDetailsDto.getUserId()),
                        userDetails.getName().getFirstName(), userDetails.getName().getMiddleName(),
                        userDetails.getName().getLastName(), userDetails.getMobileNumber().getPhoneNumber(),
                        userDetails.getMobileNumber().getCountryCode());
                return new UserUpdateResponse(userDetailsDto, UPDATED_USER);
            } else {
                throw new UserNotVerifiedException();
            }
        } else {
            throw new UserNotFoundException(userDetailsDto.getUserId());
        }
    }


    private boolean isUserPresent(String userId) {
        Optional<UserTb> findUser = userRepository.findById(UUID.fromString(userId));
        return findUser.isPresent();
    }

    private boolean isUserAuthenticated(String userId) {
        Optional<UserTb> findUser = userRepository.findById(UUID.fromString(userId));
        return findUser.isPresent() && findUser.get().getIsAuthenticated();
    }

    private boolean verifyOtp(String otp, String phoneNumber) throws Exception {
        String getOtp = otpGenerator.getCacheOtp(phoneNumber);
        otpGenerator.clearOtp(phoneNumber);
        return getOtp.equals(otp);
    }

    private UserVerification verifiedUser(VerifyUser verifyUser) throws Exception {
        userRepository.updateAuthenticatedByUserId(UUID.fromString(verifyUser.getUserId()), true);
        return UserVerification.builder().accessToken(createAuthenticationToken(verifyUser)).message(OTP_SUCCESS).build();
    }

    public String createAuthenticationToken(VerifyUser verifyUser) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(verifyUser.getUserId(), DEFAULT_PASSWORD));
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Incorrect username or password", e);
        }
        final UserDetails userDetails = userAuthService.loadUserByUsername(verifyUser.getUserId());
        return jwtUtil.generateToken(userDetails);
    }

    private UUID getUserId(String authorisation) {
        List<String> stringList = List.of(authorisation.split(" "));
        return UUID.fromString(jwtUtil.extractUsername(stringList.get(1)));
    }
}
