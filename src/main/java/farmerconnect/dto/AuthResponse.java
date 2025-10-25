package farmerconnect.dto;

import farmerconnect.enums.UserRole;
import lombok.Data;

@Data
public class AuthResponse {
    private String token;
    private String name;
    private UserRole role;
    private Integer userId;
}