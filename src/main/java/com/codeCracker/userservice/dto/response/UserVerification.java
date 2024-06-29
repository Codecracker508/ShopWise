package com.codeCracker.userservice.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserVerification {
    private String accessToken;
    private String message;
}
