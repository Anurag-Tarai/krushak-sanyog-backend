package farmerconnect.controller;

import farmerconnect.enums.UserRole;
import farmerconnect.model.Comment;
import farmerconnect.model.Product;
import farmerconnect.model.User;
import farmerconnect.dto.CommentRequest;
import farmerconnect.dto.CommentResponseDTO;
import farmerconnect.repository.CommentRepository;
import farmerconnect.repository.ProductRepository;
import farmerconnect.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("ecom/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    // ✅ Create a new comment
    @PostMapping("/add")
    public Comment createComment(@RequestBody CommentRequest commentRequest) {
        Optional<User> userOpt = userRepository.findById(commentRequest.getUserId());
        Optional<Product> productOpt = productRepository.findById(commentRequest.getProductId());

        if (userOpt.isEmpty() || productOpt.isEmpty()) {
            throw new RuntimeException("User or Product not found");
        }

        Comment comment = new Comment();
        comment.setUser(userOpt.get());
        comment.setProduct(productOpt.get());
        comment.setContent(commentRequest.getContent());
        comment.setTimestamp(LocalDateTime.now());


        return commentRepository.save(comment);
    }

    // ✅ Fetch all comments for a specific product (buyer view)
    // ✅ Fetch all comments for a specific product (buyer view)
    @GetMapping("/product/{productId}")
    public List<CommentResponseDTO> getAllCommentsByProduct(@PathVariable Integer productId) {
        List<Comment> comments = commentRepository.findByProduct_ProductId(productId);

        return comments.stream()
                .map(comment -> {
                    Integer userId = comment.getUser().getUserId();
                    String username = comment.getUser().getUsername(); // Get the username
                    String role = String.valueOf(userRepository.findById(userId)
                            .map(User::getRole)
                            .orElse(UserRole.valueOf("UNKNOWN")));

                    return new CommentResponseDTO(
                            comment.getCommentId(),
                            comment.getContent(),
                            comment.getTimestamp(),
                            userId,
                            comment.getProduct().getProductId(),
                            role,
                            username // Pass the username to the DTO
                    );
                })
                .collect(Collectors.toList());
    }



    // ✅ Fetch all comments made by buyers (ROLE_USER) across all products
    @GetMapping("/buyer")
    public List<Comment> getAllCommentsByBuyers() {
        return commentRepository.findAll().stream()
                .filter(comment -> comment.getUser().getRole().name().equals("ROLE_USER"))
                .toList();
    }


    // ✅ Fetch farmer's own comments for a product
    @GetMapping("/product/{productId}/user/{userId}")
    public List<Comment> getFarmerComments(@PathVariable Integer productId,
                                           @PathVariable Integer userId) {
        Optional<User> userOpt = userRepository.findById(userId);

        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        User user = userOpt.get();
        if (user.getRole().name().equals("ROLE_USER")) {
            // Buyer sees all comments
            return commentRepository.findByProduct_ProductId(productId);
        } else {
            // Farmer sees only their own comments
            return commentRepository.findByProduct_ProductIdAndUser_UserId(productId, userId);
        }
    }
}
