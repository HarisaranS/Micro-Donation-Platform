package com.microdonation.service;

import com.microdonation.dto.CampaignDTO;
import com.microdonation.model.Campaign;
import com.microdonation.model.User;
import com.microdonation.repository.CampaignRepository;
import com.microdonation.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CampaignService {

    private final CampaignRepository campaignRepository;
    private final UserRepository userRepository;

    public CampaignDTO createCampaign(CampaignDTO campaignDTO) {
        User creator = userRepository.findById(campaignDTO.getCreatedBy())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + campaignDTO.getCreatedBy()));

        if (campaignDTO.getEndDate().isBefore(campaignDTO.getStartDate())) {
            throw new RuntimeException("End date must be after start date");
        }

        Campaign campaign = new Campaign();
        campaign.setTitle(campaignDTO.getTitle());
        campaign.setDescription(campaignDTO.getDescription());
        campaign.setGoalAmount(campaignDTO.getGoalAmount());
        campaign.setRaisedAmount(BigDecimal.ZERO);
        campaign.setStartDate(campaignDTO.getStartDate());
        campaign.setEndDate(campaignDTO.getEndDate());
        campaign.setStatus("ACTIVE");
        campaign.setCreator(creator);

        Campaign savedCampaign = campaignRepository.save(campaign);
        return convertToDTO(savedCampaign);
    }

    public List<CampaignDTO> getAllCampaigns() {
        return campaignRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<CampaignDTO> getActiveCampaigns() {
        return campaignRepository.findActiveCampaigns().stream()
                .filter(Campaign::isActive)
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public CampaignDTO getCampaignById(Long id) {
        Campaign campaign = campaignRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Campaign not found with id: " + id));
        return convertToDTO(campaign);
    }

    public List<CampaignDTO> getCampaignsByUser(Long userId) {
        return campaignRepository.findByCreatorUserId(userId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public CampaignDTO updateCampaign(Long id, CampaignDTO campaignDTO) {
        Campaign campaign = campaignRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Campaign not found with id: " + id));

        campaign.setTitle(campaignDTO.getTitle());
        campaign.setDescription(campaignDTO.getDescription());
        campaign.setGoalAmount(campaignDTO.getGoalAmount());
        campaign.setStartDate(campaignDTO.getStartDate());
        campaign.setEndDate(campaignDTO.getEndDate());

        if (campaignDTO.getStatus() != null) {
            campaign.setStatus(campaignDTO.getStatus());
        }

        Campaign updatedCampaign = campaignRepository.save(campaign);
        return convertToDTO(updatedCampaign);
    }

    public void deleteCampaign(Long id) {
        if (!campaignRepository.existsById(id)) {
            throw new RuntimeException("Campaign not found with id: " + id);
        }
        campaignRepository.deleteById(id);
    }

    public void updateCampaignRaisedAmount(Long campaignId, BigDecimal amount) {
        Campaign campaign = campaignRepository.findById(campaignId)
                .orElseThrow(() -> new RuntimeException("Campaign not found"));

        campaign.setRaisedAmount(campaign.getRaisedAmount().add(amount));

        // Check if goal is reached
        if (campaign.getRaisedAmount().compareTo(campaign.getGoalAmount()) >= 0) {
            campaign.setStatus("COMPLETED");
        }

        campaignRepository.save(campaign);
    }

    private CampaignDTO convertToDTO(Campaign campaign) {
        CampaignDTO dto = new CampaignDTO();
        dto.setCampaignId(campaign.getCampaignId());
        dto.setTitle(campaign.getTitle());
        dto.setDescription(campaign.getDescription());
        dto.setGoalAmount(campaign.getGoalAmount());
        dto.setRaisedAmount(campaign.getRaisedAmount());
        dto.setStartDate(campaign.getStartDate());
        dto.setEndDate(campaign.getEndDate());
        dto.setStatus(campaign.getStatus());
        dto.setCreatedBy(campaign.getCreator().getUserId());
        dto.setCreatorName(campaign.getCreator().getName());
        dto.setProgressPercentage(campaign.getProgressPercentage());
        return dto;
    }
}
