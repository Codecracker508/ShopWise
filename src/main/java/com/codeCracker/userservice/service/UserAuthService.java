package com.codeCracker.userservice.service;

import com.codeCracker.userservice.entity.UserTb;
import com.codeCracker.userservice.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;

import static com.codeCracker.userservice.constants.ApplicationConstants.DEFAULT_PASSWORD;

@Service
public class UserAuthService implements UserDetailsService {
    @Autowired
    private UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String uuid) throws UsernameNotFoundException {
        UserTb user = repository.findById(UUID.fromString(uuid)).orElseThrow();
        return new User(user.getUserId().toString(), DEFAULT_PASSWORD,
                new ArrayList<>());
    }
}