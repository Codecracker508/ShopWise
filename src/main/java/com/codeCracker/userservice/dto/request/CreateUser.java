package com.codeCracker.userservice.dto.request;

import com.codeCracker.userservice.dto.model.MobileNumber;
import com.codeCracker.userservice.dto.model.Name;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateUser {
    @Valid
    @Schema(name = "name", description = "User details", requiredMode = Schema.RequiredMode.REQUIRED)
    private Name name;

    @Valid
    @Schema(name = "mobile", description = "User mobile number", requiredMode = Schema.RequiredMode.REQUIRED)
    private MobileNumber mobile;
}
