package farmerconnect.controller;

import farmerconnect.model.Wishlist;
import farmerconnect.service.WishlistService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/wishlist")
@RequiredArgsConstructor
public class WishlistController {

    private final WishlistService wishlistService;

    // ➕ Add product to wishlist
    @PostMapping("/add")
    public ResponseEntity<Wishlist> addProductToWishlist(@RequestParam Integer productId) {
        Wishlist wishlist = wishlistService.addProduct(productId);
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
    public ResponseEntity<Wishlist> getAllWishlistItems() {
        Wishlist wishlist = wishlistService.getAll();
        if (wishlist == null || wishlist.getItems().isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(wishlist);
    }
}
