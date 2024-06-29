package com.codeCracker.userservice.dto.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Name {

    @Size(min = 1, max = 100, message = "firstName is required")
    @NotNull
    @Schema(name = "firstName", description = "This contains firstName of the user.", example = "John", requiredMode = Schema.RequiredMode.REQUIRED)
    private String firstName;

    @Size(max = 100)
    @Schema(name = "middleName", description = "This contains middleName of the user.", example = "Gates")
    private String middleName;

    @Size(max = 100)
    @Schema(name = "lastName", description = "This contains lastName of the user.", example = "Abraham")
    private String lastName;
}
