package com.codeCracker.userservice.converter;

import com.codeCracker.userservice.dto.model.UserDetailsDto;
import com.codeCracker.userservice.dto.request.CreateUser;
import com.codeCracker.userservice.entity.UserTb;
import org.springframework.stereotype.Component;

@Component
public class UserConverter {
    public UserTb convertUserToEntity(CreateUser createUser) {
        UserTb userTb = new UserTb();
        userTb.setName(createUser.getName());
        userTb.setMobileNumber(createUser.getMobile());
        userTb.setIsAuthenticated(Boolean.FALSE);
        return userTb;
    }

    public UserDetailsDto userToUserDetails(UserTb userTb) {
        return new UserDetailsDto(userTb.getUserId().toString(), userTb.getName(), userTb.getMobileNumber());
    }

    public UserTb userDtoToUserTb(UserDetailsDto userDetailsDto, UserTb userTb) {
        userTb.setName(userDetailsDto.getName());
        userTb.setMobileNumber(userDetailsDto.getMobileNumber());
        return userTb;
    }
}
