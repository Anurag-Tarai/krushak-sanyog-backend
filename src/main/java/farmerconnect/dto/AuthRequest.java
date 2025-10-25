package farmerconnect.dto;

import farmerconnect.enums.UserRole;
import lombok.Data;

@Data
public class AuthRequest {
    private String email;
    private String password;
    private UserRole role;
}