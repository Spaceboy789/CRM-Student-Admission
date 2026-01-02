package com.example.StAdCRM.service;

import com.example.StAdCRM.dto.ImportResponse;
import com.example.StAdCRM.entity.Lead;
import com.example.StAdCRM.repository.LeadRepository;
import com.example.StAdCRM.util.ExcelHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.util.List;

@Service
public class LeadService {

    @Autowired
    private LeadRepository leadRepository;

    /**
     * Import leads from Excel file
     * Returns summary of import operation
     */
    public ImportResponse importLeads(MultipartFile file) {
        try {
            // Parse Excel file
            List<Lead> leads = ExcelHelper.parseExcelFile(file.getInputStream());
            
            int totalRows = leads.size();
            int importedCount = 0;
            int skippedCount = 0;
            int errorCount = 0;
            
            // Process each lead
            for (Lead lead : leads) {
                try {
                    // Check if phone already exists
                    if (leadRepository.findByPhone(lead.getPhone()).isPresent()) {
                        skippedCount++;
                        continue;
                    }
                    
                    // Save new lead
                    leadRepository.save(lead);
                    importedCount++;
                    
                } catch (Exception e) {
                    errorCount++;
                    System.err.println("Error saving lead with phone " + lead.getPhone() + ": " + e.getMessage());
                }
            }
            
            return new ImportResponse(totalRows, importedCount, skippedCount, errorCount);
            
        } catch (Exception e) {
            throw new RuntimeException("Failed to import leads: " + e.getMessage(), e);
        }
    }

    /**
     * Export leads to Excel file
     * Role-based filtering: ADMIN gets all leads, COUNSELOR gets only assigned leads
     */
    public ByteArrayInputStream exportLeads(String role, String counselor) {
        List<Lead> leads;
        
        if ("ADMIN".equals(role)) {
            // Admin can export all leads
            leads = leadRepository.findAll();
        } else if ("COUNSELOR".equals(role) && counselor != null) {
            // Counselor can only export their assigned leads
            leads = leadRepository.findByAssignedCounselor(counselor);
        } else {
            throw new IllegalArgumentException("Invalid role or missing counselor information");
        }
        
        return ExcelHelper.generateExcelFile(leads);
    }
}
