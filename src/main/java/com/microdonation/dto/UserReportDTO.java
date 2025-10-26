package com.microdonation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserReportDTO {

    private Long userId;
    private String name;
    private String email;
    private BigDecimal totalDonated;
    private Long totalDonations;
    private List<DonationDTO> donationHistory;
}
