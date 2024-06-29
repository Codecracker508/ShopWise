package com.codeCracker.userservice.entity;

import com.codeCracker.userservice.dto.model.MobileNumber;
import com.codeCracker.userservice.dto.model.Name;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserTb {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID userId;

    @Column
    @Embedded
    private Name name;

    @Column
    @Embedded
    private MobileNumber mobileNumber;

    @Column
    private Boolean isAuthenticated;
}
