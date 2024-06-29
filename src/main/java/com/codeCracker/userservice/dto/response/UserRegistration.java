package com.codeCracker.userservice.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import static com.codeCracker.userservice.constants.ApplicationConstants.WELCOME_USER;


@Data
@Builder
public class UserRegistration {
    @JsonProperty("userId")
    @Schema(name = "userId", description = "This contains userId.", example = "7ec27bc8-ece8-488b-8fa8-a6bc279ba2a2")
    private String userId;

    @JsonProperty("otp")
    @Schema(name = "otp", description = "This contains otp.", example = "111111")
    private String otp;

    @JsonProperty("message")
    @Schema(name = "message", description = "This contains respective message.", example = WELCOME_USER)
    private String message;
}
