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
    public void testGenerateOtp() {
        String phoneNo = "1234567890";
        OTPResponse otpResponse = otpGenerator.generateOtp(phoneNo);

        assertNotNull(otpResponse.getOtp());
        assertEquals(WELCOME_USER, otpResponse.getOtpMessage());
    }

    @Test
    public void testGetCacheOtp() {
        String phoneNo = "1234567890";
        otpGenerator.generateOtp(phoneNo);
        String cachedOtp = otpGenerator.getCacheOtp(phoneNo);

        assertNotNull(cachedOtp);
        assertEquals(cachedOtp, otpGenerator.getCacheOtp(phoneNo));
    }

    @Test
    public void testClearOtp() {
        String phoneNo = "1234567890";
        otpGenerator.generateOtp(phoneNo);
        otpGenerator.clearOtp(phoneNo);

        assertEquals("", otpGenerator.getCacheOtp(phoneNo));
    }

    @Test
    public void testGetCacheOtpNotFound() {
        String phoneNo = "1234567890";
        assertEquals("", otpGenerator.getCacheOtp(phoneNo));
    }
}
