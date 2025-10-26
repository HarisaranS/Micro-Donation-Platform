package com.microdonation.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CampaignDTO {

    private Long campaignId;

    @NotBlank(message = "Campaign title is required")
    @Size(min = 5, max = 100)
    private String title;

    @NotBlank(message = "Description is required")
    @Size(min = 20)
    private String description;

    @NotNull(message = "Goal amount is required")
    @DecimalMin(value = "100.0", message = "Goal amount must be at least 100")
    private BigDecimal goalAmount;

    private BigDecimal raisedAmount;

    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    private LocalDate endDate;

    private String status;

    private Long createdBy;

    private String creatorName;

    private Double progressPercentage;
}
