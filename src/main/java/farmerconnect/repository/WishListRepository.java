package farmerconnect.repository;

import farmerconnect.model.WishList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishListRepository extends JpaRepository<WishList, Long> {
    WishList findByUser_UserId(Integer userId);

}
