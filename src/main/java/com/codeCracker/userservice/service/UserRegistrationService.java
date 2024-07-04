package com.codeCracker.userservice.service;

import com.codeCracker.userservice.dto.model.UserDetailsDto;
import com.codeCracker.userservice.dto.request.CreateUser;
import com.codeCracker.userservice.dto.request.VerifyUser;
import com.codeCracker.userservice.dto.response.UserRegistration;
import com.codeCracker.userservice.dto.response.UserUpdateResponse;
import com.codeCracker.userservice.dto.response.UserVerification;
import com.codeCracker.userservice.exceptions.UserNotFoundException;
import com.codeCracker.userservice.exceptions.UserNotVerifiedException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserRegistrationService {
    UserRegistration userRegistration(CreateUser createUser);

    UserVerification userVerification(VerifyUser verifyUser) throws Exception;

    List<UserDetailsDto> getAllUsers();

    UserDetailsDto getUser(String authorisation) throws UserNotFoundException;

    UserUpdateResponse updateUser(UserDetailsDto userDetailsDto) throws UserNotFoundException, UserNotVerifiedException;
}
