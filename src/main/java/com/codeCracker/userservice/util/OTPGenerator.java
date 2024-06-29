package com.codeCracker.userservice.util;

import com.codeCracker.userservice.dto.model.OTPResponse;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static com.codeCracker.userservice.constants.ApplicationConstants.EXPIRE_MIN;
import static com.codeCracker.userservice.constants.ApplicationConstants.WELCOME_USER;

@Component
public class OTPGenerator {
    private final LoadingCache<String, String> otpCache;

    public OTPGenerator() {
        this.otpCache = CacheBuilder.newBuilder()
                .expireAfterWrite(EXPIRE_MIN, TimeUnit.MINUTES)
                .build(new CacheLoader<>() {
                    @Override
                    public String load(String s) {
                        return "";
                    }
                });
    }

    public OTPResponse generateOtp(String phoneNo) {
        return OTPResponse.builder().otp(getRandomOTP(phoneNo)).otpMessage(WELCOME_USER).build();
    }

    private String getRandomOTP(String phoneNo) {
        String otp = new DecimalFormat("000000")
                .format(new Random().nextInt(999999));
        this.otpCache.put(phoneNo, otp);
        return otp;
    }

    //get saved otp
    public String getCacheOtp(String key) {
        try {
            return otpCache.get(key);
        } catch (Exception e) {
            return "";
        }
    }

    //clear stored otp
    public void clearOtp(String key) {
        this.otpCache.invalidate(key);
    }
}