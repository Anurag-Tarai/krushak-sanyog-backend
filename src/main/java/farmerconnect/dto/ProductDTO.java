package farmerconnect.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
public class ProductDTO {
    private String name;
    private String description;
    private Double quantity;
    private String category;
    private Double latitude;
    private Double longitude;
    private String address;
    private String variants;

}
