package farmerconnect.service;

import farmerconnect.exception.ProductNotFoundException;
import farmerconnect.exception.ResourceNotFoundException;
import farmerconnect.model.Product;
import farmerconnect.model.WishList;
import farmerconnect.model.WishListItem;
import farmerconnect.repository.ProductRepository;
import farmerconnect.repository.WishListItemRepository;
import farmerconnect.repository.WishListRepository;
import farmerconnect.security.CustomUserDetails;
import farmerconnect.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class  WishListService {

    private final UserService userService;
    private final WishListRepository wishlistRepository;
    private final WishListItemRepository wishlistItemRepository;
    private final ProductRepository productRepository;

    public WishList addProduct(Integer productId) {
        CustomUserDetails authUserDetails = userService.getCurrentAuthUser();
        Integer userId = authUserDetails.getUserId();

        WishList wishlist = wishlistRepository.findByUser_UserId(userId);
        if (wishlist == null) {
            wishlist = new WishList();
            wishlist.setUser(userService.getUserById(userId));
            wishlist.setItems(new ArrayList<>());
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        boolean alreadyExists = wishlist.getItems().stream()
                .anyMatch(item -> item.getProduct().getProductId().equals(productId));

        if (!alreadyExists) {
            WishListItem item = new WishListItem();
            item.setProduct(product);
            item.setWishlist(wishlist);
            wishlist.getItems().add(item);
        }

        return wishlistRepository.save(wishlist);
    }


    public void removeProduct(Integer productId) {
        CustomUserDetails userDetails = userService.getCurrentAuthUser();
        Integer userId = userDetails.getUserId();

        WishList wishlist = wishlistRepository.findByUser_UserId(userId);
        if (wishlist == null) throw new ResourceNotFoundException("Cart is empty");

        if (!wishlist.getItems().removeIf(item -> item.getProduct().getProductId().equals(productId))) {
            throw new ProductNotFoundException("Product Not Found");
        }
        wishlistRepository.save(wishlist);
    }

    public WishList getAll() {
        CustomUserDetails userDetails = userService.getCurrentAuthUser();
        return wishlistRepository.findByUser_UserId(userDetails.getUserId());
    }
}
