package farmerconnect.repository;

import farmerconnect.model.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
    Wishlist findByUser_UserId(Integer userId);

}
