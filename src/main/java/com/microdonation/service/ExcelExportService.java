package com.microdonation.service;

import com.microdonation.dto.CampaignReportDTO;
import com.microdonation.dto.DonationDTO;
import com.microdonation.dto.UserReportDTO;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class ExcelExportService {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#,##0.00");

    /**
     * Generate Campaign Report Excel
     */
    public byte[] generateCampaignReportExcel(CampaignReportDTO report) throws IOException {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            // Create Campaign Summary Sheet
            Sheet summarySheet = workbook.createSheet("Campaign Summary");
            createCampaignSummary(summarySheet, report, workbook);

            // Create Donations Details Sheet
            Sheet donationsSheet = workbook.createSheet("Recent Donations");
            createDonationsSheet(donationsSheet, report.getRecentDonations(), workbook);

            // Create Top Donors Sheet
            Sheet topDonorsSheet = workbook.createSheet("Top Donors");
            createDonationsSheet(topDonorsSheet, report.getTopDonations(), workbook);

            workbook.write(out);
            return out.toByteArray();
        }
    }

    /**
     * Generate User Report Excel
     */
    public byte[] generateUserReportExcel(UserReportDTO report) throws IOException {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            // Create User Summary Sheet
            Sheet summarySheet = workbook.createSheet("User Summary");
            createUserSummary(summarySheet, report, workbook);

            // Create Donation History Sheet
            Sheet historySheet = workbook.createSheet("Donation History");
            createDonationsSheet(historySheet, report.getDonationHistory(), workbook);

            workbook.write(out);
            return out.toByteArray();
        }
    }

    /**
     * Create Campaign Summary Sheet
     */
    private void createCampaignSummary(Sheet sheet, CampaignReportDTO report, Workbook workbook) {
        CellStyle headerStyle = createHeaderStyle(workbook);
        CellStyle dataStyle = createDataStyle(workbook);

        int rowNum = 0;

        // Title
        Row titleRow = sheet.createRow(rowNum++);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue("CAMPAIGN FUNDRAISING REPORT");
        CellStyle titleStyle = workbook.createCellStyle();
        Font titleFont = workbook.createFont();
        titleFont.setBold(true);
        titleFont.setFontHeightInPoints((short) 16);
        titleStyle.setFont(titleFont);
        titleCell.setCellStyle(titleStyle);

        rowNum++; // Empty row

        // Campaign Details
        addDataRow(sheet, rowNum++, "Campaign ID:", report.getCampaignId().toString(), headerStyle, dataStyle);
        addDataRow(sheet, rowNum++, "Title:", report.getTitle(), headerStyle, dataStyle);
        addDataRow(sheet, rowNum++, "Description:", report.getDescription(), headerStyle, dataStyle);
        addDataRow(sheet, rowNum++, "Goal Amount:", "₹ " + DECIMAL_FORMAT.format(report.getGoalAmount()), headerStyle, dataStyle);
        addDataRow(sheet, rowNum++, "Raised Amount:", "₹ " + DECIMAL_FORMAT.format(report.getRaisedAmount()), headerStyle, dataStyle);
        addDataRow(sheet, rowNum++, "Progress:", String.format("%.2f%%", report.getProgressPercentage()), headerStyle, dataStyle);
        addDataRow(sheet, rowNum++, "Total Donors:", report.getTotalDonors().toString(), headerStyle, dataStyle);
        addDataRow(sheet, rowNum++, "Status:", report.getStatus(), headerStyle, dataStyle);
        addDataRow(sheet, rowNum++, "Start Date:", report.getStartDate(), headerStyle, dataStyle);
        addDataRow(sheet, rowNum++, "End Date:", report.getEndDate(), headerStyle, dataStyle);

        // Auto-size columns
        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);
    }

    /**
     * Create User Summary Sheet
     */
    private void createUserSummary(Sheet sheet, UserReportDTO report, Workbook workbook) {
        CellStyle headerStyle = createHeaderStyle(workbook);
        CellStyle dataStyle = createDataStyle(workbook);

        int rowNum = 0;

        // Title
        Row titleRow = sheet.createRow(rowNum++);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue("USER DONATION REPORT");
        CellStyle titleStyle = workbook.createCellStyle();
        Font titleFont = workbook.createFont();
        titleFont.setBold(true);
        titleFont.setFontHeightInPoints((short) 16);
        titleStyle.setFont(titleFont);
        titleCell.setCellStyle(titleStyle);

        rowNum++; // Empty row

        // User Details
        addDataRow(sheet, rowNum++, "User ID:", report.getUserId().toString(), headerStyle, dataStyle);
        addDataRow(sheet, rowNum++, "Name:", report.getName(), headerStyle, dataStyle);
        addDataRow(sheet, rowNum++, "Email:", report.getEmail(), headerStyle, dataStyle);
        addDataRow(sheet, rowNum++, "Total Donated:", "₹ " + DECIMAL_FORMAT.format(report.getTotalDonated()), headerStyle, dataStyle);
        addDataRow(sheet, rowNum++, "Total Donations:", report.getTotalDonations().toString(), headerStyle, dataStyle);

        // Auto-size columns
        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);
    }

    /**
     * Create Donations Sheet (for both recent donations and top donations)
     */
    private void createDonationsSheet(Sheet sheet, java.util.List<DonationDTO> donations, Workbook workbook) {
        CellStyle headerStyle = createHeaderStyle(workbook);
        CellStyle dataStyle = createDataStyle(workbook);

        // Create header row
        Row headerRow = sheet.createRow(0);
        String[] headers = {"Donation ID", "User Name", "Campaign Title", "Amount (₹)", "Date", "Payment Status", "Payment Mode", "Transaction ID"};

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        // Create data rows
        int rowNum = 1;
        for (DonationDTO donation : donations) {
            Row row = sheet.createRow(rowNum++);

            row.createCell(0).setCellValue(donation.getDonationId());
            row.createCell(1).setCellValue(donation.getUserName());
            row.createCell(2).setCellValue(donation.getCampaignTitle());
            row.createCell(3).setCellValue(DECIMAL_FORMAT.format(donation.getAmount()));
            row.createCell(4).setCellValue(donation.getDonationDate().format(DATE_FORMATTER));
            row.createCell(5).setCellValue(donation.getPaymentStatus());
            row.createCell(6).setCellValue(donation.getPaymentMode() != null ? donation.getPaymentMode() : "N/A");
            row.createCell(7).setCellValue(donation.getTransactionId() != null ? donation.getTransactionId() : "N/A");

            // Apply data style to all cells
            for (int i = 0; i < 8; i++) {
                row.getCell(i).setCellStyle(dataStyle);
            }
        }

        // Auto-size all columns
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    /**
     * Helper method to add a data row with label and value
     */
    private void addDataRow(Sheet sheet, int rowNum, String label, String value, CellStyle headerStyle, CellStyle dataStyle) {
        Row row = sheet.createRow(rowNum);
        Cell labelCell = row.createCell(0);
        labelCell.setCellValue(label);
        labelCell.setCellStyle(headerStyle);

        Cell valueCell = row.createCell(1);
        valueCell.setCellValue(value);
        valueCell.setCellStyle(dataStyle);
    }

    /**
     * Create header cell style
     */
    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 12);
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }

    /**
     * Create data cell style
     */
    private CellStyle createDataStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setWrapText(true);
        return style;
    }
}
