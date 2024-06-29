package com.codeCracker.userservice.dto.request;

import com.codeCracker.userservice.dto.model.MobileNumber;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VerifyUser {

    @NotNull(message = "userId should not be null.")
    @Schema(name = "userId", description = "The Identification details of the user.", requiredMode = Schema.RequiredMode.REQUIRED)
    private String userId;

    @NotNull(message = "otp should not be null.")
    @Schema(name = "otp", description = "One Time Password. This is used for authentication.", requiredMode = Schema.RequiredMode.REQUIRED)
    private String otp;

    @Valid
    @Schema(name = "phone", description = "User mobile number", requiredMode = Schema.RequiredMode.REQUIRED)
    private MobileNumber mobile;
}
