package farmerconnect.dto;

import farmerconnect.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.auth.signer.params.TokenSignerParams;

@Data

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private String name;
    private UserRole role;
}