package com.codeCracker.userservice.constants;

public class ErrorConstants {
    public static class ErrorCodes {
        public static final String DEFAULT_ERROR_CODE = "100000";
        public static final String VALIDATION_ERROR = "100001";
        public static final String USER_NOT_FOUND_ERROR = "100002";

        public static final String INVALID_OTP = "100003";
    }

    public static class ReasonCodes {
        public static final String INVALID_INPUT_VALUE = "INVALID_INPUT_VALUE";
        public static final String INTERNAL_SERVER_ERROR = "INTERNAL_SERVER_ERROR";
    }
}
