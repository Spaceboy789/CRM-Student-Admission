package com.example.StAdCRM.util;

import com.example.StAdCRM.entity.Lead;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ExcelHelper {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Parse Excel file and return list of Lead objects
     * Expected columns: Name | Phone | Email | InterestedCourse | Source
     */
    public static List<Lead> parseExcelFile(InputStream inputStream) {
        List<Lead> leads = new ArrayList<>();
        
        try (Workbook workbook = new XSSFWorkbook(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);
            
            // Skip header row (row 0)
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;
                
                try {
                    Lead lead = new Lead();
                    
                    // Column 0: Name
                    Cell nameCell = row.getCell(0);
                    if (nameCell != null) {
                        lead.setName(getCellValueAsString(nameCell));
                    }
                    
                    // Column 1: Phone (mandatory)
                    Cell phoneCell = row.getCell(1);
                    if (phoneCell != null) {
                        String phone = getCellValueAsString(phoneCell);
                        if (phone != null && !phone.trim().isEmpty()) {
                            lead.setPhone(phone.trim());
                        } else {
                            continue; // Skip row if phone is empty
                        }
                    } else {
                        continue; // Skip row if phone is missing
                    }
                    
                    // Column 2: Email
                    Cell emailCell = row.getCell(2);
                    if (emailCell != null) {
                        lead.setEmail(getCellValueAsString(emailCell));
                    }
                    
                    // Column 3: InterestedCourse
                    Cell courseCell = row.getCell(3);
                    if (courseCell != null) {
                        lead.setInterestedCourse(getCellValueAsString(courseCell));
                    }
                    
                    // Column 4: Source
                    Cell sourceCell = row.getCell(4);
                    if (sourceCell != null) {
                        lead.setSource(getCellValueAsString(sourceCell));
                    }
                    
                    // Set default status
                    lead.setStatus("NEW");
                    
                    leads.add(lead);
                    
                } catch (Exception e) {
                    // Skip invalid rows gracefully
                    System.err.println("Error parsing row " + i + ": " + e.getMessage());
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse Excel file: " + e.getMessage(), e);
        }
        
        return leads;
    }

    /**
     * Generate Excel file from list of leads
     * Columns: LeadId | Name | Phone | Email | InterestedCourse | Status | AssignedCounselor | CreatedDate
     */
    public static ByteArrayInputStream generateExcelFile(List<Lead> leads) {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Leads");
            
            // Create header row
            Row headerRow = sheet.createRow(0);
            String[] columns = {"LeadId", "Name", "Phone", "Email", "InterestedCourse", "Status", "AssignedCounselor", "CreatedDate"};
            
            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
                
                // Bold header
                CellStyle headerStyle = workbook.createCellStyle();
                Font font = workbook.createFont();
                font.setBold(true);
                headerStyle.setFont(font);
                cell.setCellStyle(headerStyle);
            }
            
            // Fill data rows
            int rowNum = 1;
            for (Lead lead : leads) {
                Row row = sheet.createRow(rowNum++);
                
                row.createCell(0).setCellValue(lead.getId() != null ? lead.getId() : 0);
                row.createCell(1).setCellValue(lead.getName() != null ? lead.getName() : "");
                row.createCell(2).setCellValue(lead.getPhone() != null ? lead.getPhone() : "");
                row.createCell(3).setCellValue(lead.getEmail() != null ? lead.getEmail() : "");
                row.createCell(4).setCellValue(lead.getInterestedCourse() != null ? lead.getInterestedCourse() : "");
                row.createCell(5).setCellValue(lead.getStatus() != null ? lead.getStatus() : "");
                row.createCell(6).setCellValue(lead.getAssignedCounselor() != null ? lead.getAssignedCounselor() : "");
                row.createCell(7).setCellValue(lead.getCreatedDate() != null ? lead.getCreatedDate().format(DATE_FORMATTER) : "");
            }
            
            // Auto-size columns
            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }
            
            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
            
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate Excel file: " + e.getMessage(), e);
        }
    }

    /**
     * Helper method to get cell value as string
     */
    private static String getCellValueAsString(Cell cell) {
        if (cell == null) return null;
        
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getLocalDateTimeCellValue().format(DATE_FORMATTER);
                } else {
                    // Convert numeric to string (handle phone numbers as text)
                    double numericValue = cell.getNumericCellValue();
                    if (numericValue == (long) numericValue) {
                        return String.valueOf((long) numericValue);
                    } else {
                        return String.valueOf(numericValue);
                    }
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }
}
