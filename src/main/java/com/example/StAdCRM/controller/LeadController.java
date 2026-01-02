package com.example.StAdCRM.controller;

import com.example.StAdCRM.dto.ImportResponse;
import com.example.StAdCRM.service.LeadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;

@RestController
@RequestMapping("/leads")
@CrossOrigin(origins = "*")
public class LeadController {

    @Autowired
    private LeadService leadService;

    /**
     * POST /leads/import
     * Import leads from Excel file
     * 
     * @param file - Excel file (.xlsx)
     * @param role - User role (ADMIN or COUNSELOR)
     * @return ImportResponse with summary statistics
     */
    @PostMapping("/import")
    public ResponseEntity<?> importLeads(
            @RequestParam("file") MultipartFile file,
            @RequestParam("role") String role) {
        
        try {
            // Validate file
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("File is empty");
            }
            
            // Validate file extension
            String filename = file.getOriginalFilename();
            if (filename == null || !filename.endsWith(".xlsx")) {
                return ResponseEntity.badRequest().body("Only .xlsx files are supported");
            }
            
            // Validate role
            if (!"ADMIN".equals(role) && !"COUNSELOR".equals(role)) {
                return ResponseEntity.badRequest().body("Invalid role. Must be ADMIN or COUNSELOR");
            }
            
            // Import leads
            ImportResponse response = leadService.importLeads(file);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error importing leads: " + e.getMessage());
        }
    }

    /**
     * GET /leads/export
     * Export leads to Excel file
     * 
     * @param role - User role (ADMIN or COUNSELOR)
     * @param counselor - Counselor name (required for COUNSELOR role)
     * @return Excel file download
     */
    @GetMapping("/export")
    public ResponseEntity<?> exportLeads(
            @RequestParam("role") String role,
            @RequestParam(value = "counselor", required = false) String counselor) {
        
        try {
            // Validate role
            if (!"ADMIN".equals(role) && !"COUNSELOR".equals(role)) {
                return ResponseEntity.badRequest().body("Invalid role. Must be ADMIN or COUNSELOR");
            }
            
            // Validate counselor parameter for COUNSELOR role
            if ("COUNSELOR".equals(role) && (counselor == null || counselor.trim().isEmpty())) {
                return ResponseEntity.badRequest().body("Counselor parameter is required for COUNSELOR role");
            }
            
            // Export leads
            ByteArrayInputStream excelFile = leadService.exportLeads(role, counselor);
            
            // Set headers for file download
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=leads.xlsx");
            
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                    .body(new InputStreamResource(excelFile));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error exporting leads: " + e.getMessage());
        }
    }
}
