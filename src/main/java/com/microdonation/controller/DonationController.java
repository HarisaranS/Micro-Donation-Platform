package com.microdonation.controller;

import com.microdonation.dto.DonationDTO;
import com.microdonation.service.DonationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/donations")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DonationController {

    private final DonationService donationService;

    @PostMapping
    public ResponseEntity<DonationDTO> makeDonation(@Valid @RequestBody DonationDTO donationDTO) {
        try {
            DonationDTO createdDonation = donationService.makeDonation(donationDTO);
            return new ResponseEntity<>(createdDonation, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<List<DonationDTO>> getAllDonations() {
        List<DonationDTO> donations = donationService.getAllDonations();
        return ResponseEntity.ok(donations);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DonationDTO> getDonationById(@PathVariable Long id) {
        try {
            DonationDTO donation = donationService.getDonationById(id);
            return ResponseEntity.ok(donation);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<DonationDTO>> getDonationsByUser(@PathVariable Long userId) {
        List<DonationDTO> donations = donationService.getDonationsByUser(userId);
        return ResponseEntity.ok(donations);
    }

    @GetMapping("/campaign/{campaignId}")
    public ResponseEntity<List<DonationDTO>> getDonationsByCampaign(@PathVariable Long campaignId) {
        List<DonationDTO> donations = donationService.getDonationsByCampaign(campaignId);
        return ResponseEntity.ok(donations);
    }
}
