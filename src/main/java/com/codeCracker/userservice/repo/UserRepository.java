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
}
