package com.microdonation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CampaignReportDTO {

    private Long campaignId;
    private String title;
    private String description;
    private BigDecimal goalAmount;
    private BigDecimal raisedAmount;
    private Double progressPercentage;
    private Long totalDonors;
    private String status;
    private String startDate;
    private String endDate;
    private List<DonationDTO> recentDonations;
    private List<DonationDTO> topDonations;
}
