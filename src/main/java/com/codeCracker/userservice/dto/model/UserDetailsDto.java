package com.codeCracker.userservice.dto.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailsDto {
    @Schema(name = "userId", description = "This contains userId.", example = "24474988-c83b-49d4-adbd-a4a08712e7df")
    private String userId;

    @Schema(name = "name", description = "This contains users details. For example, FirstName, LastName,..etc.")
    private Name name;

    @Schema(name = "mobileNumber", description = "This contains users mobile number")
    private MobileNumber mobileNumber;
}
