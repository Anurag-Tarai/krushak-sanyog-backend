package farmerconnect.controller;

import farmerconnect.enums.UserRole;
import farmerconnect.model.Comment;
import farmerconnect.model.Product;
import farmerconnect.model.User;
import farmerconnect.dto.CommentRequest;
import farmerconnect.dto.CommentResponseDTO;
import farmerconnect.exception.ResourceNotFoundException;
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
        User user = userRepository.findById(commentRequest.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Product product = productRepository.findById(commentRequest.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        Comment comment = new Comment();
        comment.setUser(user);
        comment.setProduct(product);
        comment.setContent(commentRequest.getContent());
        comment.setTimestamp(LocalDateTime.now());


        return commentRepository.save(comment);
    }

    // ✅ Fetch all comments for a specific product (buyer view)
    @GetMapping("/product/{productId}")
    public List<CommentResponseDTO> getAllCommentsByProduct(@PathVariable Integer productId) {
        List<Comment> comments = commentRepository.findByProduct_ProductId(productId);

        return comments.stream()
                .map(comment -> {
                    User user = comment.getUser();
                    return new CommentResponseDTO(
                            comment.getCommentId(),
                            comment.getContent(),
                            comment.getTimestamp(),
                            user.getUserId(),
                            comment.getProduct().getProductId(),
                            user.getRole().name(),
                            user.getUsername()
                    );
                })
                .collect(Collectors.toList());
    }



    // ✅ Fetch all comments made by buyers (ROLE_BUYER) across all products
    @GetMapping("/buyer")
    public List<Comment> getAllCommentsByBuyers() {
        return commentRepository.findAll().stream()
                .filter(comment -> comment.getUser().getRole() == UserRole.ROLE_BUYER)
                .toList();
    }


    // ✅ Fetch farmer's own comments for a product
    @GetMapping("/user/{userId}/product/{productId}")
    public List<Comment> getFarmerComments(@PathVariable Integer productId,
                                           @PathVariable Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (user.getRole() == UserRole.ROLE_BUYER) {
            // Buyer sees all comments
            return commentRepository.findByProduct_ProductId(productId);
        } else {
            // Farmer sees only their own comments
            return commentRepository.findByProduct_ProductIdAndUser_UserId(productId, userId);
        }
    }
}
