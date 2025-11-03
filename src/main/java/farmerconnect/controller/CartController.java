//package farmerconnect.controller;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.PutMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import farmerconnect.model.Cart;
//import farmerconnect.service.CartService;
//
//@RestController
//@RequestMapping("/api/v1/cart")
//@RequiredArgsConstructor
//public class CartController {
//
//    private final CartService cartService;
//
//    @Autowired
//    private CartRepository cartRepository;
//
//    @GetMapping("/buyer/{userId}")
//   public Integer getCartIdByUserId(@PathVariable Integer userId) {
//        Cart cart = cartRepository.findByUser_UserId(userId);
//        return cart != null ? cart.getCartId() : null;
//    }
//
//    @PostMapping("/add")
//    public ResponseEntity<Cart> addProductToCart(@RequestParam Integer userId, @RequestParam Integer productId) {
//        Cart cart = cartService.addProductToCart(userId, productId);
//        return new ResponseEntity<>(cart, HttpStatus.CREATED);
//
//    }
//
//    @DeleteMapping("/remove/{cartId}/{productId}")
//    public ResponseEntity<String> removeProductFromCart(@PathVariable Integer cartId, @PathVariable Integer productId) {
//        cartService.removeProductFromCart(cartId, productId);
//        String msg = "Product is removed from cart";
//        return new ResponseEntity<String>(msg, HttpStatus.OK);
//    }
//
//    @DeleteMapping("/empty/{cartId}")
//    public ResponseEntity<String> removeAllProductFromCart(@PathVariable Integer cartId) {
//        cartService.removeAllProductFromCart(cartId);
//        String msg = "All product Remove From cart";
//        return new ResponseEntity<String>(msg, HttpStatus.OK);
//    }
//
//    @GetMapping("/products/{cartId}")
//    public ResponseEntity<Cart> getAllCartProducts(@PathVariable Integer cartId) {
//        Cart products = cartService.getAllCartProduct(cartId);
//        return ResponseEntity.ok(products);
//    }
//}
