package com.codeCracker.userservice.constants;

public interface ErrorConstants {
    interface ErrorCodes {
        String DEFAULT_ERROR_CODE = "100000";
        String VALIDATION_ERROR = "100001";
        String USER_NOT_FOUND_ERROR = "100002";
        String INVALID_OTP = "100003";
        String USER_NOT_VERIFIED = "100004";
    }

    interface ReasonCodes {
        String INVALID_INPUT_VALUE = "INVALID_INPUT_VALUE";
        String INTERNAL_SERVER_ERROR = "INTERNAL_SERVER_ERROR";
        String UNAUTHORIZED = "INVALID_ACCESS";
    }

    interface Description {
        String USER_NOT_VERIFIED = "The provided user is not verified. Please verify the user.";
    }
}
