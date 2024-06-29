package com.codeCracker.userservice.constants;

public class ApplicationConstants {

    public static final String WELCOME_USER = "Above OTP is valid for a limited time. Please use it to complete authentication.";
    public static final String OTP_ERROR = "Error Occurred while generating OTP. Please try again after sometime/check the details you have provided";
    public static final String OTP_SUCCESS = "Otp verified successfully.";
    public static final String OTP_EXPIRY = "Otp is either expired or incorrect.";
    public static final String USER_NOT_FOUND = "Invalid userId. Please verify.";
    public static final Integer EXPIRE_MIN = 5;

    public static final String DEFAULT_PASSWORD = "P0JvsrJHrt63Hzu";

    public static final String SECRET_KEY = "secret";
    public static final String[] WHITELIST_URLS = {
            "/newUser/**",
            "/v2/api-docs",
            "/configuration/ui",
            "/swagger-resources/**",
            "/configuration/security",
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/webjars/**"
    };

    public static class Headers {
        public static final String AUTHORIZATION = "Authorization";
        public static final String CUSTOMER_ID = "customer-id";
    }

    public static class URLS {
        public static final String ALL_USERS = "/allUsers";
        public static final String NEW_USER = "/newUser/create";
        public static final String VERIFY_USER = "/newUser/verifyUser";
    }
}
