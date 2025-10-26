package com.microdonation.controller;

import com.microdonation.dto.CampaignReportDTO;
import com.microdonation.dto.UserReportDTO;
import com.microdonation.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/campaign/{campaignId}")
    public ResponseEntity<CampaignReportDTO> getCampaignReport(@PathVariable Long campaignId) {
        try {
            CampaignReportDTO report = reportService.getCampaignReport(campaignId);
            return ResponseEntity.ok(report);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<UserReportDTO> getUserReport(@PathVariable Long userId) {
        try {
            UserReportDTO report = reportService.getUserReport(userId);
            return ResponseEntity.ok(report);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
}
