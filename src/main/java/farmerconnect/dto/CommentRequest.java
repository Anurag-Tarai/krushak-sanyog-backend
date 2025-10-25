package farmerconnect.dto;

import lombok.Data;

// DTO class to accept comment request data
@Data
public class CommentRequest {
    private Integer userId;
    private Integer productId;
    private String content;
}
