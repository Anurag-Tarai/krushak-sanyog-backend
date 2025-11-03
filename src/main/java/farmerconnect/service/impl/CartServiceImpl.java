//package farmerconnect.service.impl;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import farmerconnect.security.CustomUserDetails;
//import farmerconnect.service.UserService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Service;
//import farmerconnect.exception.CartException;
//import farmerconnect.exception.ProductException;
//import farmerconnect.exception.UserException;
//import farmerconnect.model.Cart;
//import farmerconnect.model.CartItem;
//import farmerconnect.model.Product;
//import farmerconnect.model.User;
//import farmerconnect.repository.CartItemRepository;
//import farmerconnect.repository.CartRepository;
//import farmerconnect.repository.ProductRepository;
//import farmerconnect.repository.UserRepository;
//import farmerconnect.service.CartService;
//
//@Service
//@RequiredArgsConstructor
//public class CartServiceImpl implements CartService {
//
//    private  final UserService userService;
//
//	private final ProductRepository productRepository;
//
//	private final CartRepository cartRepository;
//
//	private final CartItemRepository cartItemRepository;
//
//	private final UserRepository userRepository;
//
//
//    @Override
//    public Cart addProductToCart(Integer productId) throws CartException {
//        CustomUserDetails userDetails = userService.getCurrentAuthUser();
//        Integer buyerId = userDetails.getUserId();
//        Cart cart = cartRepository.findByUserId(buyerId);
//
//        Product product = productRepository.getReferenceById(productId);
//        CartItem item = new CartItem();
//        item.setProduct(product);
//
//        if(cart!=null){
//            item.setCart(cart);
//            List<CartItem> items = cart.getCartItems();
//            items.add(item);
//            cart.setCartItems(items);
//        }else{
//            Cart cart1 = new Cart();
//            cart1.setCartItems(List.of(item));
//            Cart cart2 = CartRepository.save
//        }
//        return null;
//    }
//
//    @Override
//    public void removeProductFromCart(Integer productId) throws CartException {
//
//    }
//
//    @Override
//    public void removeAllProductFromCart() throws CartException {
//
//    }
//
//    @Override
//    public Cart getAllCartProduct() throws CartException {
//        return null;
//    }
//
//}