package com.codeCracker.userservice.exceptions;

public class UserNotFoundException extends Exception {

    public UserNotFoundException(String userId) {
        super(userId);
    }
}
