package com.codeCracker.userservice.repo;

import com.codeCracker.userservice.entity.UserTb;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserTb, UUID> {
    @Modifying
    @Transactional
    @Query(value = "update user_tb u set u.is_authenticated = ?2 where u.user_id = ?1", nativeQuery = true)
    void updateAuthenticatedByUserId(UUID uuid, Boolean authenticated);

    @Modifying
    @Transactional
    @Query(value = "UPDATE user_tb set first_name = ?2, middle_name = ?3, last_name = ?4, phone_number = ?5, country_code = ?6 WHERE user_id = ?1", nativeQuery = true)
    void updateUserByUserId(UUID id, String firstName,
                            String middleName, String lastName,
                            String mobileNumber, String countryCode);
}
