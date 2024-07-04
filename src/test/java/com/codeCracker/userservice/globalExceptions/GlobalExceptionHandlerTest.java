package com.codeCracker.userservice.globalExceptions;

import com.codeCracker.userservice.constants.ApplicationTestConstants;
import com.codeCracker.userservice.constants.ErrorConstants;
import com.codeCracker.userservice.dto.model.ShopWiseDefaultError;
import com.codeCracker.userservice.exceptions.InvalidOtpException;
import com.codeCracker.userservice.exceptions.UserNotFoundException;
import com.codeCracker.userservice.exceptions.UserNotVerifiedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Collections;

import static com.codeCracker.userservice.constants.ApplicationConstants.OTP_EXPIRY;
import static com.codeCracker.userservice.constants.ApplicationConstants.USER_NOT_FOUND;
import static com.codeCracker.userservice.constants.ApplicationTestConstants.SAMPLE_ERROR_MESSAGE;
import static com.codeCracker.userservice.constants.ErrorConstants.ErrorCodes.*;
import static com.codeCracker.userservice.constants.ErrorConstants.ReasonCodes.INTERNAL_SERVER_ERROR;
import static com.codeCracker.userservice.constants.ErrorConstants.ReasonCodes.INVALID_INPUT_VALUE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    public void setUp() {
        globalExceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    public void testHandleValidationExceptions() {
        FieldError fieldError = new FieldError("objectName", "field", "defaultMessage");
        BindingResult bindingResult = mock(BindingResult.class);
        MethodParameter methodParameter = mock(MethodParameter.class);
        when(bindingResult.getAllErrors()).thenReturn(Collections.singletonList(fieldError));
        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(
                methodParameter, bindingResult);

        ResponseEntity<ShopWiseDefaultError> response = globalExceptionHandler.handleValidationExceptions(ex);

        ShopWiseDefaultError error = response.getBody();
        assertNotNull(error);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(VALIDATION_ERROR, error.getErrorCode());
        assertEquals(INVALID_INPUT_VALUE, error.getReasonCode());
        assertEquals("defaultMessage", error.getDescription());
        assertEquals("field", error.getSource());
    }

    @Test
    public void testHandleAllExceptions() {
        Exception ex = new Exception(SAMPLE_ERROR_MESSAGE);

        ResponseEntity<ShopWiseDefaultError> response = globalExceptionHandler.handleAllExceptions(ex);

        ShopWiseDefaultError error = response.getBody();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(error);
        assertEquals(DEFAULT_ERROR_CODE, error.getErrorCode());
        assertEquals(INTERNAL_SERVER_ERROR, error.getReasonCode());
        assertEquals(SAMPLE_ERROR_MESSAGE, error.getDescription());
        assertNull(error.getSource());
    }

    @Test
    public void testUserNotFoundException() {
        UserNotFoundException ex = new UserNotFoundException(ApplicationTestConstants.SAMPLE_UUID);

        ResponseEntity<ShopWiseDefaultError> response = globalExceptionHandler.userNotFoundException(ex);

        ShopWiseDefaultError error = response.getBody();
        assertNotNull(error);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(USER_NOT_FOUND_ERROR, error.getErrorCode());
        assertEquals(INVALID_INPUT_VALUE, error.getReasonCode());
        assertEquals(USER_NOT_FOUND, error.getDescription());
        assertEquals("userId", error.getSource());
    }

    @Test
    public void testInvalidOtpException() {
        InvalidOtpException ex = new InvalidOtpException();

        ResponseEntity<ShopWiseDefaultError> response = globalExceptionHandler.invalidOtpException(ex);

        ShopWiseDefaultError error = response.getBody();
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(error);
        assertEquals(INVALID_OTP, error.getErrorCode());
        assertEquals(INVALID_INPUT_VALUE, error.getReasonCode());
        assertEquals(OTP_EXPIRY, error.getDescription());
        assertEquals("otp", error.getSource());
    }

    @Test
    public void testUserNotVerifiedException() {
        UserNotVerifiedException userNotVerifiedException = new UserNotVerifiedException();

        ResponseEntity<ShopWiseDefaultError> response =
                globalExceptionHandler.userNotVerifiedException(userNotVerifiedException);
        ShopWiseDefaultError defaultError = response.getBody();
        assertNotNull(defaultError);
        assertEquals(defaultError.getDescription(), ErrorConstants.Description.USER_NOT_VERIFIED);
        assertEquals(defaultError.getErrorCode(), USER_NOT_VERIFIED);
    }
}
