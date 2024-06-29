package com.codeCracker.userservice.components;

import org.springframework.security.crypto.password.PasswordEncoder;

public class PlainTextPasswordEncoder implements PasswordEncoder {

    private static final PasswordEncoder INSTANCE = new PlainTextPasswordEncoder();

    public static PasswordEncoder getInstance() {
        return INSTANCE;
    }

    @Override
    public String encode(CharSequence rawPassword) {
        return rawPassword.toString();
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return rawPassword.toString().equals(encodedPassword);
    }
}