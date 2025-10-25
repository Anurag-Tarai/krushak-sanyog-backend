package farmerconnect.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentResponseDTO {
    private Integer commentId;
    private String content;
    private LocalDateTime timestamp;
    private Integer userId;
    private Integer productId;
    private String role;
    private String username; // Add the username field

    // Constructor to convert from entity
    public CommentResponseDTO(Integer commentId, String content, LocalDateTime timestamp,
                              Integer userId, Integer productId, String role, String username) {
        this.commentId = commentId;
        this.content = content;
        this.timestamp = timestamp;
        this.userId = userId;
        this.productId = productId;
        this.role = role;
        this.username = username; // Set the username
    }
}
