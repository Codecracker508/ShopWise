package com.codeCracker.userservice.util;

import com.codeCracker.userservice.dto.model.OTPResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static com.codeCracker.userservice.constants.ApplicationConstants.WELCOME_USER;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class OTPGeneratorTest {

    private OTPGenerator otpGenerator;

    @BeforeEach
    public void setUp() {
        otpGenerator = new OTPGenerator();
    }

    @Test
    public void testGenerateOtp_validPhoneNumber() {
        String phoneNumber = "1234567890";
        OTPResponse response = otpGenerator.generateOtp(phoneNumber);
        assertNotNull(response);
        assertEquals(6, response.getOtp().length());
        assertEquals(WELCOME_USER, response.getOtpMessage());
    }

    @Test
    public void testGetCacheOtp_validPhoneNumber() throws Exception {
        String phoneNumber = "1234567890";
        otpGenerator.generateOtp(phoneNumber);
        String otp = otpGenerator.getCacheOtp(phoneNumber);
        assertNotNull(otp);
        assertEquals(6, otp.length());
    }

    @Test
    public void testGenerateAndRetrieveMultipleOtps() throws Exception {
        String phoneNumber1 = "1234567890";
        String phoneNumber2 = "0987654321";
        otpGenerator.generateOtp(phoneNumber1);
        otpGenerator.generateOtp(phoneNumber2);
        String otp1 = otpGenerator.getCacheOtp(phoneNumber1);
        String otp2 = otpGenerator.getCacheOtp(phoneNumber2);
        assertNotNull(otp1);
        assertNotNull(otp2);
        assertNotEquals(otp1, otp2);
    }

    @Test
    public void testClearOtp() throws Exception {
        String phoneNumber = "1234567890";
        otpGenerator.generateOtp(phoneNumber);
        otpGenerator.clearOtp(phoneNumber);
        Exception exception = assertThrows(Exception.class, () -> otpGenerator.getCacheOtp(phoneNumber));
        assertNotNull(exception);
    }

    @Test
    public void testGetCacheOtp_nonExistentPhoneNumber() {
        Exception exception = assertThrows(Exception.class, () -> otpGenerator.getCacheOtp("1111111111"));
        assertNotNull(exception);
    }
}
