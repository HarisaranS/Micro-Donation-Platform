package com.microdonation.service;

import com.microdonation.dto.CampaignReportDTO;
import com.microdonation.dto.DonationDTO;
import com.microdonation.dto.UserReportDTO;
import com.microdonation.model.Campaign;
import com.microdonation.model.Donation;
import com.microdonation.model.User;
import com.microdonation.repository.CampaignRepository;
import com.microdonation.repository.DonationRepository;
import com.microdonation.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportService {

    private final CampaignRepository campaignRepository;
    private final UserRepository userRepository;
    private final DonationRepository donationRepository;

    public CampaignReportDTO getCampaignReport(Long campaignId) {
        Campaign campaign = campaignRepository.findById(campaignId)
                .orElseThrow(() -> new RuntimeException("Campaign not found"));

        List<Donation> donations = donationRepository.findByCampaignCampaignId(campaignId);
        Long totalDonors = donationRepository.getCountOfDonorsByCampaign(campaignId);

        List<DonationDTO> recentDonations = donations.stream()
                .filter(d -> "PAID".equals(d.getPaymentStatus()))
                .sorted((d1, d2) -> d2.getDonationDate().compareTo(d1.getDonationDate()))
                .limit(10)
                .map(this::convertDonationToDTO)
                .collect(Collectors.toList());

        List<DonationDTO> topDonations = donations.stream()
                .filter(d -> "PAID".equals(d.getPaymentStatus()))
                .sorted((d1, d2) -> d2.getAmount().compareTo(d1.getAmount()))
                .limit(5)
                .map(this::convertDonationToDTO)
                .collect(Collectors.toList());

        CampaignReportDTO report = new CampaignReportDTO();
        report.setCampaignId(campaign.getCampaignId());
        report.setTitle(campaign.getTitle());
        report.setDescription(campaign.getDescription());
        report.setGoalAmount(campaign.getGoalAmount());
        report.setRaisedAmount(campaign.getRaisedAmount());
        report.setProgressPercentage(campaign.getProgressPercentage());
        report.setTotalDonors(totalDonors);
        report.setStatus(campaign.getStatus());
        report.setStartDate(campaign.getStartDate().toString());
        report.setEndDate(campaign.getEndDate().toString());
        report.setRecentDonations(recentDonations);
        report.setTopDonations(topDonations);

        return report;
    }

    public UserReportDTO getUserReport(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Donation> donations = donationRepository.findByUserUserId(userId);

        BigDecimal totalDonated = donations.stream()
                .filter(d -> "PAID".equals(d.getPaymentStatus()))
                .map(Donation::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        List<DonationDTO> donationHistory = donations.stream()
                .sorted((d1, d2) -> d2.getDonationDate().compareTo(d1.getDonationDate()))
                .map(this::convertDonationToDTO)
                .collect(Collectors.toList());

        UserReportDTO report = new UserReportDTO();
        report.setUserId(user.getUserId());
        report.setName(user.getName());
        report.setEmail(user.getEmail());
        report.setTotalDonated(totalDonated);
        report.setTotalDonations((long) donationHistory.size());
        report.setDonationHistory(donationHistory);

        return report;
    }

    private DonationDTO convertDonationToDTO(Donation donation) {
        DonationDTO dto = new DonationDTO();
        dto.setDonationId(donation.getDonationId());
        dto.setUserId(donation.getUser().getUserId());
        dto.setUserName(donation.getUser().getName());
        dto.setCampaignId(donation.getCampaign().getCampaignId());
        dto.setCampaignTitle(donation.getCampaign().getTitle());
        dto.setAmount(donation.getAmount());
        dto.setDonationDate(donation.getDonationDate());
        dto.setPaymentStatus(donation.getPaymentStatus());
        dto.setPaymentMode(donation.getPaymentMode());
        dto.setTransactionId(donation.getTransactionId());
        return dto;
    }
}
