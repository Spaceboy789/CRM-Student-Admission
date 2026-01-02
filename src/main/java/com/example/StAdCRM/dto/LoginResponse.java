package com.example.StAdCRM.dto;

import com.example.StAdCRM.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {
    private String message;
    private Role role;
    private String token; // Dummy token for now
    private Long userId;
}
