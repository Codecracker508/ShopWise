package com.codeCracker.userservice.components;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static com.codeCracker.userservice.constants.ApplicationTestConstants.TEST_PASSWORD;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PlainTextPasswordEncoderTest {

    private final PlainTextPasswordEncoder plainTextPasswordEncoder = new PlainTextPasswordEncoder();

    private final CharSequence charSequence = TEST_PASSWORD;

    @Test
    void encode() {
        assertEquals(TEST_PASSWORD, plainTextPasswordEncoder.encode(charSequence));
        assertNotEquals("", plainTextPasswordEncoder.encode(charSequence));
    }

    @Test
    void matches() {
        assertTrue(plainTextPasswordEncoder.matches(charSequence, TEST_PASSWORD));
        assertNotEquals(false, plainTextPasswordEncoder.matches(charSequence, TEST_PASSWORD));
    }

    @Test
    void getInstance() {
        assertThat(PlainTextPasswordEncoder.getInstance().getClass()).isEqualTo(PlainTextPasswordEncoder.class);
    }
}