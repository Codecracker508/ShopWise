package com.codeCracker.userservice.converter;

import com.codeCracker.userservice.constants.ApplicationTestConstants;
import com.codeCracker.userservice.dto.model.MobileNumber;
import com.codeCracker.userservice.dto.model.Name;
import com.codeCracker.userservice.dto.model.UserDetailsDto;
import com.codeCracker.userservice.dto.request.CreateUser;
import com.codeCracker.userservice.entity.UserTb;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.UUID;

@SpringBootTest
class UserConverterTest {

    UserTb userTb;
    CreateUser createUser;
    UserDetailsDto userDetailsDto;

    @MockBean
    private UserConverter userConverter;

    @BeforeEach
    void setUp() {
        userConverter = new UserConverter();
        userTb = new UserTb(
                UUID.fromString(ApplicationTestConstants.SAMPLE_UUID),
                new Name("John", "M", "Gates"),
                new MobileNumber("+91", "1234567890"),
                false
        );
        createUser = new CreateUser(
                userTb.getName(),
                userTb.getMobileNumber()
        );
        userDetailsDto = new UserDetailsDto(
                userTb.getUserId().toString(),
                userTb.getName(),
                userTb.getMobileNumber()
        );
    }

    @Test
    void convertUserToEntity() {
        UserTb userDetails = userConverter.convertUserToEntity(createUser);
        Assertions.assertThat(userDetails.getName()).isEqualTo(createUser.getName());
    }

    @Test
    void userToUserDetails() {
        UserDetailsDto userDetails = userConverter.userToUserDetails(userTb);
        Assertions.assertThat(userDetails.getUserId()).isEqualTo(userTb.getUserId().toString());
    }

}