package com.codeCracker.userservice.exceptions;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class UserNotVerifiedExceptionTest {
    @Test
    void testUserNotVerifiedException() {
        UserNotVerifiedException userNotVerifiedException = new UserNotVerifiedException();
        assertThat(userNotVerifiedException.getClass()).isEqualTo(UserNotVerifiedException.class);
    }
}