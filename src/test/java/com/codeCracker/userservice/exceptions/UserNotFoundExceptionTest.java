package com.codeCracker.userservice.exceptions;

import com.codeCracker.userservice.constants.ApplicationTestConstants;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class UserNotFoundExceptionTest {

    @Test
    void testUserNotFoundException() {
        UserNotFoundException userNotFoundException = new UserNotFoundException(
                ApplicationTestConstants.SAMPLE_UUID
        );
        assertThat(userNotFoundException.getClass()).isEqualTo(UserNotFoundException.class);
    }

}