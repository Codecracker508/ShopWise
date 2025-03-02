package com.codeCracker.userservice.constants;

public interface ApplicationConstants {

    String WELCOME_USER = "Above OTP is valid for a limited time. Please use it to complete authentication.";
    String OTP_ERROR =
            "Error Occurred while generating OTP. Please try again after sometime/check the details you have provided";
    String OTP_SUCCESS = "Otp verified successfully.";
    String OTP_EXPIRY = "Otp is either expired or incorrect.";
    String USER_NOT_FOUND = "Invalid userId. Please verify.";
    String UPDATED_USER = "Details Updated successfully";
    String UNAUTHORIZED_ACCESS = "Unauthorized Access. Please verify.";
    Integer EXPIRE_MIN = 10;

    String DEFAULT_PASSWORD = "P0JvsrJHrt63Hzu";

    String SECRET_KEY = "&@=@5fs;IyMAHmq";

    interface Headers {
        String AUTHORIZATION = "Authorization";
        String INTERNAL_REQUEST = "X-Internal-Request";
    }

    interface URLS {
        String ALL_USERS = "/allUsers";
        String NEW_USER = "/newUser/create";
        String VERIFY_USER = "/newUser/verifyUser";
        String GET_USER = "/profile";
        String UPDATE_USER = "/profile/update";
    }
}
