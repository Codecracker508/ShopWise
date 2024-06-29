package com.codeCracker.userservice.dto.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MobileNumber {

    @Size(min = 3, max = 3, message = "country code length must be 3 characters.\n Example: +91")
    @Schema(name = "countryCode", description = "Country code", example = "+91", requiredMode = Schema.RequiredMode.REQUIRED)
    private String countryCode;

    @Size(min = 10, max = 10, message = "Phone number must be 10 characters. \n Please verify.")
    @Schema(name = "phoneNumber", description = "Phone number", example = "1234567890", requiredMode = Schema.RequiredMode.REQUIRED)
    private String phoneNumber;
}
