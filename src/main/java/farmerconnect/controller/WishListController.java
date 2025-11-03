package farmerconnect.controller;

import farmerconnect.model.WishList;
import farmerconnect.service.WishListService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/wishlist")
@RequiredArgsConstructor
public class WishListController {

    private final WishListService wishlistService;

    // ➕ Add product to wishlist
    @PostMapping("/add")
    public ResponseEntity<WishList> addProductToWishlist(@RequestParam Integer productId) {
        WishList wishlist = wishlistService.addProduct(productId);
        return ResponseEntity.status(HttpStatus.CREATED).body(wishlist);
    }

    // ❌ Remove product from wishlist
    @DeleteMapping("/remove")
    public ResponseEntity<String> removeProductFromWishlist(@RequestParam Integer productId) {
        wishlistService.removeProduct(productId);
        return ResponseEntity.ok("Product removed from wishlist");
    }

    // 📋 Get all wishlist items
    @GetMapping("/all")
    public ResponseEntity<WishList> getAllWishlistItems() {
        WishList wishlist = wishlistService.getAll();
        if (wishlist == null || wishlist.getItems().isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(wishlist);
    }
}
