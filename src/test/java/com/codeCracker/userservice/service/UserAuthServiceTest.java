package com.codeCracker.userservice.service;

import com.codeCracker.userservice.constants.ApplicationTestConstants;
import com.codeCracker.userservice.entity.UserTb;
import com.codeCracker.userservice.repo.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static com.codeCracker.userservice.constants.ApplicationConstants.DEFAULT_PASSWORD;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserAuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserAuthService userAuthService;

    private UUID sampleUUID;
    private UserTb userTb;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        sampleUUID = UUID.fromString(ApplicationTestConstants.SAMPLE_UUID);
        userTb = new UserTb();
        userTb.setUserId(sampleUUID);
    }

    @Test
    void loadUserByUsername_UserFound() {
        when(userRepository.findById(sampleUUID)).thenReturn(Optional.of(userTb));

        UserDetails userDetails = userAuthService.loadUserByUsername(sampleUUID.toString());

        assertNotNull(userDetails);
        assertEquals(sampleUUID.toString(), userDetails.getUsername());
        assertEquals(DEFAULT_PASSWORD, userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().isEmpty());

        verify(userRepository, times(1)).findById(sampleUUID);
    }

    @Test
    void loadUserByUsername_UserNotFound() {
        when(userRepository.findById(sampleUUID)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> userAuthService.loadUserByUsername(sampleUUID.toString()));

        verify(userRepository, times(1)).findById(sampleUUID);
    }
}
