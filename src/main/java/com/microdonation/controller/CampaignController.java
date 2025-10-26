package com.microdonation.controller;

import com.microdonation.dto.CampaignDTO;
import com.microdonation.service.CampaignService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/campaigns")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CampaignController {

    private final CampaignService campaignService;

    @PostMapping
    public ResponseEntity<CampaignDTO> createCampaign(@Valid @RequestBody CampaignDTO campaignDTO) {
        try {
            CampaignDTO createdCampaign = campaignService.createCampaign(campaignDTO);
            return new ResponseEntity<>(createdCampaign, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<List<CampaignDTO>> getAllCampaigns() {
        List<CampaignDTO> campaigns = campaignService.getAllCampaigns();
        return ResponseEntity.ok(campaigns);
    }

    @GetMapping("/active")
    public ResponseEntity<List<CampaignDTO>> getActiveCampaigns() {
        List<CampaignDTO> campaigns = campaignService.getActiveCampaigns();
        return ResponseEntity.ok(campaigns);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CampaignDTO> getCampaignById(@PathVariable Long id) {
        try {
            CampaignDTO campaign = campaignService.getCampaignById(id);
            return ResponseEntity.ok(campaign);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<CampaignDTO>> getCampaignsByUser(@PathVariable Long userId) {
        List<CampaignDTO> campaigns = campaignService.getCampaignsByUser(userId);
        return ResponseEntity.ok(campaigns);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CampaignDTO> updateCampaign(@PathVariable Long id, @Valid @RequestBody CampaignDTO campaignDTO) {
        try {
            CampaignDTO updatedCampaign = campaignService.updateCampaign(id, campaignDTO);
            return ResponseEntity.ok(updatedCampaign);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCampaign(@PathVariable Long id) {
        try {
            campaignService.deleteCampaign(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
