package farmerconnect.dto;

// package com.farmerconnect.dto;

import farmerconnect.enums.UserRole;
import lombok.Data;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Data
public class UserRegisterDTO {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private UserRole role;  // ROLE_USER or ROLE_FARMER
}
