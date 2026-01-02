package com.example.StAdCRM.util;

import com.example.StAdCRM.entity.Lead;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Utility to create a sample Excel file for testing lead import
 */
public class SampleExcelGenerator {

    public static void main(String[] args) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Leads");
            
            // Create header row
            Row headerRow = sheet.createRow(0);
            String[] headers = {"Name", "Phone", "Email", "InterestedCourse", "Source"};
            
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                
                // Bold header
                CellStyle style = workbook.createCellStyle();
                Font font = workbook.createFont();
                font.setBold(true);
                style.setFont(font);
                cell.setCellStyle(style);
            }
            
            // Sample data
            Object[][] sampleData = {
                {"John Doe", "9876543210", "john.doe@email.com", "Computer Science", "Website"},
                {"Jane Smith", "9876543211", "jane.smith@email.com", "Business Administration", "Referral"},
                {"Mike Johnson", "9876543212", "mike.j@email.com", "Engineering", "Social Media"},
                {"Sarah Williams", "9876543213", "sarah.w@email.com", "Medicine", "Walk-in"},
                {"David Brown", "9876543214", "david.b@email.com", "Law", "Phone Call"}
            };
            
            // Fill data rows
            for (int i = 0; i < sampleData.length; i++) {
                Row row = sheet.createRow(i + 1);
                Object[] rowData = sampleData[i];
                
                for (int j = 0; j < rowData.length; j++) {
                    Cell cell = row.createCell(j);
                    cell.setCellValue(rowData[j].toString());
                }
            }
            
            // Auto-size columns
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }
            
            // Write to file
            try (FileOutputStream fileOut = new FileOutputStream("sample_leads.xlsx")) {
                workbook.write(fileOut);
                System.out.println("Sample Excel file created: sample_leads.xlsx");
            }
            
        } catch (IOException e) {
            System.err.println("Error creating sample file: " + e.getMessage());
        }
    }
}
