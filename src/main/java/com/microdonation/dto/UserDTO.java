package com.microdonation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long userId;
    private String name;
    private String email;
    private String contactNo;
    private String password; // Only used during registration
    private String role;
    private LocalDateTime joinDate;

    // *** NEW FIELD: Wallet Balance ***
    private BigDecimal walletBalance;
}
