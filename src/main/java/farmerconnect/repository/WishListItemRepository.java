package farmerconnect.repository;

import farmerconnect.model.WishListItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishListItemRepository extends JpaRepository<WishListItem, Long> {
}
