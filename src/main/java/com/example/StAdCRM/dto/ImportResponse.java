package com.example.StAdCRM.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImportResponse {
    private int totalRows;
    private int importedCount;
    private int skippedCount;
    private int errorCount;
}
