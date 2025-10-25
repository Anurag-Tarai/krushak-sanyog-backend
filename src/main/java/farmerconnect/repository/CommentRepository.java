package farmerconnect.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import farmerconnect.model.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {

    // Get all comments for a specific product
    List<Comment> findByProduct_ProductId(Integer productId);

    // Get comments for a specific product by a specific user
    List<Comment> findByProduct_ProductIdAndUser_UserId(Integer productId, Integer userId);
}
