package com.microdonation.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DonationDTO {

    private Long donationId;

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Campaign ID is required")
    private Long campaignId;

    @NotNull(message = "Donation amount is required")
    @DecimalMin(value = "1.0", message = "Minimum donation amount is 1")
    private BigDecimal amount;

    private LocalDateTime donationDate;

    private String paymentStatus;

    private String paymentMode;

    private String transactionId;

    private String userName;

    private String campaignTitle;
}
