package com.microdonation.service;

import com.microdonation.dto.DonationDTO;
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
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class DonationService {

    private final DonationRepository donationRepository;
    private final UserRepository userRepository;
    private final CampaignRepository campaignRepository;
    private final CampaignService campaignService;

    public DonationDTO makeDonation(DonationDTO donationDTO) {
        User user = userRepository.findById(donationDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Campaign campaign = campaignRepository.findById(donationDTO.getCampaignId())
                .orElseThrow(() -> new RuntimeException("Campaign not found"));

        if (!campaign.isActive()) {
            throw new RuntimeException("Campaign is not active");
        }

        if (donationDTO.getAmount().compareTo(BigDecimal.ONE) < 0) {
            throw new RuntimeException("Minimum donation amount is 1");
        }

        Donation donation = new Donation();
        donation.setUser(user);
        donation.setCampaign(campaign);
        donation.setAmount(donationDTO.getAmount());
        donation.setPaymentMode(donationDTO.getPaymentMode());
        donation.setPaymentStatus("PAID"); // Simulating successful payment
        donation.setTransactionId(generateTransactionId());

        Donation savedDonation = donationRepository.save(donation);

        // Update campaign raised amount
        campaignService.updateCampaignRaisedAmount(campaign.getCampaignId(), donation.getAmount());

        return convertToDTO(savedDonation);
    }

    public List<DonationDTO> getAllDonations() {
        return donationRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public DonationDTO getDonationById(Long id) {
        Donation donation = donationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Donation not found with id: " + id));
        return convertToDTO(donation);
    }

    public List<DonationDTO> getDonationsByUser(Long userId) {
        return donationRepository.findByUserUserId(userId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<DonationDTO> getDonationsByCampaign(Long campaignId) {
        return donationRepository.findByCampaignCampaignId(campaignId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public BigDecimal getTotalDonationsByUser(Long userId) {
        List<Donation> donations = donationRepository.findByUserUserId(userId);
        return donations.stream()
                .filter(d -> "PAID".equals(d.getPaymentStatus()))
                .map(Donation::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private String generateTransactionId() {
        return "TXN" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private DonationDTO convertToDTO(Donation donation) {
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
