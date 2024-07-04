package com.codeCracker.userservice.repo;

import com.codeCracker.userservice.constants.ApplicationTestConstants;
import com.codeCracker.userservice.dto.model.MobileNumber;
import com.codeCracker.userservice.dto.model.Name;
import com.codeCracker.userservice.entity.UserTb;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class UserRepositoryTest {

    AutoCloseable autoCloseable;
    @Mock
    private UserRepository userRepository;
    private UserTb userTb;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        userTb = new UserTb(
                UUID.fromString(ApplicationTestConstants.SAMPLE_UUID),
                new Name("John", "M", "Gates"),
                new MobileNumber("+91", "123456789"),
                false
        );
    }

    @Test
    void updateAuthenticatedByUserId() {
        userRepository.updateAuthenticatedByUserId(userTb.getUserId(), true);
        Optional<UserTb> userTbOptional = userRepository.findById(userTb.getUserId());
        userTbOptional.ifPresent(tb -> assertTrue(tb.getIsAuthenticated()));
    }

    @Test
    void testUpdateUserByUserId() {
        userRepository.updateUserByUserId(userTb.getUserId(),
                userTb.getName().getFirstName(),
                "Dan",
                userTb.getName().getLastName(),
                userTb.getMobileNumber().getPhoneNumber(),
                userTb.getMobileNumber().getCountryCode());
        Optional<UserTb> userTbOptional = userRepository.findById(userTb.getUserId());
        userTbOptional.ifPresent(user -> assertEquals(user.getName().getMiddleName(), "Dan"));
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteById(userTb.getUserId());
    }
}