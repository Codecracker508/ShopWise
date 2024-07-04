package com.codeCracker.userservice.globalExceptions;

import com.codeCracker.userservice.constants.ErrorConstants;
import com.codeCracker.userservice.dto.model.ShopWiseDefaultError;
import com.codeCracker.userservice.exceptions.InvalidOtpException;
import com.codeCracker.userservice.exceptions.UserNotFoundException;
import com.codeCracker.userservice.exceptions.UserNotVerifiedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static com.codeCracker.userservice.constants.ApplicationConstants.OTP_EXPIRY;
import static com.codeCracker.userservice.constants.ApplicationConstants.USER_NOT_FOUND;
import static com.codeCracker.userservice.constants.ErrorConstants.ErrorCodes.*;
import static com.codeCracker.userservice.constants.ErrorConstants.ReasonCodes.INTERNAL_SERVER_ERROR;
import static com.codeCracker.userservice.constants.ErrorConstants.ReasonCodes.INVALID_INPUT_VALUE;

@ControllerAdvice
public class GlobalExceptionHandler {

    private final ShopWiseDefaultError defaultError = new ShopWiseDefaultError();

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ShopWiseDefaultError> handleValidationExceptions(MethodArgumentNotValidException ex) {
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            defaultError.setErrorCode(VALIDATION_ERROR);
            defaultError.setReasonCode(INVALID_INPUT_VALUE);
            defaultError.setDescription(errorMessage);
            defaultError.setSource(fieldName);
        });

        return new ResponseEntity<>(defaultError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ShopWiseDefaultError> handleAllExceptions(Exception ex) {
        defaultError.setSource(null);
        defaultError.setErrorCode(DEFAULT_ERROR_CODE);
        defaultError.setReasonCode(INTERNAL_SERVER_ERROR);
        defaultError.setDescription(ex.getMessage());
        return new ResponseEntity<>(defaultError, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ShopWiseDefaultError> userNotFoundException(UserNotFoundException userNotFoundException) {
        defaultError.setDescription(USER_NOT_FOUND);
        defaultError.setErrorCode(USER_NOT_FOUND_ERROR);
        defaultError.setSource("userId");
        defaultError.setReasonCode(INVALID_INPUT_VALUE);
        return new ResponseEntity<>(defaultError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidOtpException.class)
    public ResponseEntity<ShopWiseDefaultError> invalidOtpException(InvalidOtpException invalidOtpException) {
        defaultError.setDescription(OTP_EXPIRY);
        defaultError.setErrorCode(INVALID_OTP);
        defaultError.setSource("otp");
        defaultError.setReasonCode(INVALID_INPUT_VALUE);
        return new ResponseEntity<>(defaultError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotVerifiedException.class)
    public ResponseEntity<ShopWiseDefaultError> userNotVerifiedException(UserNotVerifiedException userNotVerifiedException) {
        defaultError.setErrorCode(USER_NOT_VERIFIED);
        defaultError.setDescription(ErrorConstants.Description.USER_NOT_VERIFIED);
        defaultError.setSource("userId");
        defaultError.setReasonCode(ErrorConstants.ReasonCodes.UNAUTHORIZED);
        return new ResponseEntity<>(defaultError, HttpStatus.BAD_REQUEST);
    }
}
