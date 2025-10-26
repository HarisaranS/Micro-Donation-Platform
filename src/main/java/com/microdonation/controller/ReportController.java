package com.microdonation.controller;

import com.microdonation.dto.CampaignReportDTO;
import com.microdonation.dto.UserReportDTO;
import com.microdonation.service.ExcelExportService;
import com.microdonation.service.PdfExportService;
import com.microdonation.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ReportController {

    private final ReportService reportService;
    private final PdfExportService pdfExportService;
    private final ExcelExportService excelExportService;

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

    /**
     * Export Campaign Report as PDF
     */
    @GetMapping("/campaign/{campaignId}/export/pdf")
    public ResponseEntity<byte[]> exportCampaignReportPdf(@PathVariable Long campaignId) {
        try {
            CampaignReportDTO report = reportService.getCampaignReport(campaignId);
            byte[] pdfBytes = pdfExportService.generateCampaignReportPdf(report);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment",
                    "campaign_report_" + campaignId + ".pdf");
            headers.setContentLength(pdfBytes.length);

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Export Campaign Report as Excel
     */
    @GetMapping("/campaign/{campaignId}/export/excel")
    public ResponseEntity<byte[]> exportCampaignReportExcel(@PathVariable Long campaignId) {
        try {
            CampaignReportDTO report = reportService.getCampaignReport(campaignId);
            byte[] excelBytes = excelExportService.generateCampaignReportExcel(report);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
            headers.setContentDispositionFormData("attachment",
                    "campaign_report_" + campaignId + ".xlsx");
            headers.setContentLength(excelBytes.length);

            return new ResponseEntity<>(excelBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Export User Report as PDF
     */
    @GetMapping("/user/{userId}/export/pdf")
    public ResponseEntity<byte[]> exportUserReportPdf(@PathVariable Long userId) {
        try {
            UserReportDTO report = reportService.getUserReport(userId);
            byte[] pdfBytes = pdfExportService.generateUserReportPdf(report);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment",
                    "user_report_" + userId + ".pdf");
            headers.setContentLength(pdfBytes.length);

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Export User Report as Excel
     */
    @GetMapping("/user/{userId}/export/excel")
    public ResponseEntity<byte[]> exportUserReportExcel(@PathVariable Long userId) {
        try {
            UserReportDTO report = reportService.getUserReport(userId);
            byte[] excelBytes = excelExportService.generateUserReportExcel(report);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
            headers.setContentDispositionFormData("attachment",
                    "user_report_" + userId + ".xlsx");
            headers.setContentLength(excelBytes.length);

            return new ResponseEntity<>(excelBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
