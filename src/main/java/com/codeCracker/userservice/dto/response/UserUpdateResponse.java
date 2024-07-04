package com.codeCracker.userservice.dto.response;

import com.codeCracker.userservice.dto.model.UserDetailsDto;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserUpdateResponse {
    private UserDetailsDto userDetails;
    private String message;
}
