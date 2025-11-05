package farmerconnect.service;

import farmerconnect.exception.ProductAlreadyInWishlistException;
import farmerconnect.exception.ProductNotFoundException;
import farmerconnect.exception.ResourceNotFoundException;
import farmerconnect.model.Product;
import farmerconnect.model.Wishlist;
import farmerconnect.model.WishlistItem;
import farmerconnect.repository.ProductRepository;
import farmerconnect.repository.WishlistItemRepository;
import farmerconnect.repository.WishlistRepository;
import farmerconnect.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class WishlistService {

    private final UserService userService;
    private final WishlistRepository wishlistRepository;
    private final ProductRepository productRepository;

    public Wishlist addProduct(Integer productId) {
        CustomUserDetails authUserDetails = userService.getCurrentAuthUser();
        Integer userId = authUserDetails.getUserId();

        Wishlist wishlist = wishlistRepository.findByUser_UserId(userId);
        if (wishlist == null) {
            wishlist = new Wishlist();
            wishlist.setUser(userService.getUserById(userId));
            wishlist.setItems(new ArrayList<>());
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        boolean alreadyExists = wishlist.getItems().stream()
                .anyMatch(item -> item.getProduct().getProductId().equals(productId));


        if (alreadyExists) {
            throw new ProductAlreadyInWishlistException("Product already exists in wishlist");
        }

            WishlistItem item = new WishlistItem();
            item.setProduct(product);
            item.setWishlist(wishlist);
            wishlist.getItems().add(item);


        return wishlistRepository.save(wishlist);
    }


    public void removeProduct(Integer productId) {
        CustomUserDetails userDetails = userService.getCurrentAuthUser();
        Integer userId = userDetails.getUserId();

        Wishlist wishlist = wishlistRepository.findByUser_UserId(userId);
        if (wishlist == null) throw new ResourceNotFoundException("Cart is empty");

        if (!wishlist.getItems().removeIf(item -> item.getProduct().getProductId().equals(productId))) {
            throw new ProductNotFoundException("Product Not Found");
        }
        wishlistRepository.save(wishlist);
    }

    public Wishlist getAll() {
        CustomUserDetails userDetails = userService.getCurrentAuthUser();
        return wishlistRepository.findByUser_UserId(userDetails.getUserId());
    }
}
