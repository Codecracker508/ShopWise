package com.codeCracker.userservice.exceptions;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class InvalidOtpExceptionTest {
    @Test
    void testInvalidOtpException() {
        InvalidOtpException invalidOtpException = new InvalidOtpException();
        assertThat(invalidOtpException.getClass()).isEqualTo(InvalidOtpException.class);
    }
}