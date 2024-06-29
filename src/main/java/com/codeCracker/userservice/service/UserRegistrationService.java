package com.codeCracker.userservice.service;

import com.codeCracker.userservice.dto.model.UserDetailsDto;
import com.codeCracker.userservice.dto.request.CreateUser;
import com.codeCracker.userservice.dto.request.VerifyUser;
import com.codeCracker.userservice.dto.response.UserRegistration;
import com.codeCracker.userservice.dto.response.UserVerification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserRegistrationService {
    UserRegistration userRegistration(CreateUser createUser);

    UserVerification userVerification(VerifyUser verifyUser) throws Exception;

    List<UserDetailsDto> getAllUsers();
}
