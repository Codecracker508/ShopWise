package com.codeCracker.userservice.rest;

import com.codeCracker.userservice.constants.ApplicationConstants;
import com.codeCracker.userservice.dto.model.UserDetailsDto;
import com.codeCracker.userservice.dto.request.CreateUser;
import com.codeCracker.userservice.dto.request.VerifyUser;
import com.codeCracker.userservice.dto.response.UserRegistration;
import com.codeCracker.userservice.dto.response.UserUpdateResponse;
import com.codeCracker.userservice.dto.response.UserVerification;
import com.codeCracker.userservice.exceptions.UserNotFoundException;
import com.codeCracker.userservice.exceptions.UserNotVerifiedException;
import com.codeCracker.userservice.service.UserRegistrationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserLoginController {

    @Autowired
    private UserRegistrationService userRegistrationService;

    @PostMapping(value = ApplicationConstants.URLS.NEW_USER,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserRegistration> userRegistration(@Valid @RequestBody CreateUser createUser) {
        return new ResponseEntity<>(userRegistrationService.userRegistration(createUser), HttpStatus.CREATED);
    }

    @PostMapping(value = ApplicationConstants.URLS.VERIFY_USER,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserVerification> userVerification(@Valid @RequestBody VerifyUser verifyUser) throws Exception {
        return new ResponseEntity<>(userRegistrationService.userVerification(verifyUser), HttpStatus.OK);
    }

    @GetMapping(value = ApplicationConstants.URLS.ALL_USERS,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserDetailsDto>> getAllUsers() {
        return new ResponseEntity<>(userRegistrationService.getAllUsers(), HttpStatus.OK);
    }

    @GetMapping(value = ApplicationConstants.URLS.GET_USER,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDetailsDto> getUser(@RequestHeader(ApplicationConstants.Headers.AUTHORIZATION)
                                                  String authorization) throws UserNotFoundException {
        return new ResponseEntity<>(userRegistrationService.getUser(authorization), HttpStatus.OK);
    }

    @PostMapping(value = ApplicationConstants.URLS.UPDATE_USER,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserUpdateResponse> updateUser(@RequestBody UserDetailsDto userDetailsDto) throws UserNotFoundException, UserNotVerifiedException {
        return new ResponseEntity<>(userRegistrationService.updateUser(userDetailsDto), HttpStatus.OK);
    }
}
