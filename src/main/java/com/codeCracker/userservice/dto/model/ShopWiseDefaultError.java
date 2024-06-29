package com.codeCracker.userservice.dto.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ShopWiseDefaultError {
    private String ErrorCode;
    private String ReasonCode;
    private String Source;
    private String Description;
}
