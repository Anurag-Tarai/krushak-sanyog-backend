//package farmerconnect.service.impl;
//
//import java.util.List;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import farmerconnect.exception.ReviewException;
//import farmerconnect.model.Product;
//import farmerconnect.model.Review;
//import farmerconnect.model.User;
//import farmerconnect.repository.ProductRepository;
//import farmerconnect.repository.ReviewRepository;
//import farmerconnect.repository.UserRepository;
//import farmerconnect.service.ReviewService;
//
//@Service
//@RequiredArgsConstructor
//public class ReviewServiceImpl implements ReviewService {
//
//	private final ProductRepository productRepository;
//
//	private final ReviewRepository reviewRepository;
//
//	private final UserRepository userRepository;
//
//	@Override
//	public Review addReviewToProduct(Integer productId, Integer userId, Review review) throws ReviewException {
//		Product existingProduct = productRepository.findById(productId)
//				.orElseThrow(() -> new ReviewException("Product Not Found"));
//
//		User existingUser = userRepository.findById(userId)
//				.orElseThrow(() -> new ReviewException("User Not Found In Database"));
//
//		existingUser.getReviews().add(review);
//		review.setUser(existingUser);
//		existingProduct.getReviews().add(review);
//		review.setProduct(existingProduct);
//		userRepository.save(existingUser);
//		productRepository.save(existingProduct);
//
//		return reviewRepository.save(review);
//	}
//
//	@Override
//	public Review updateReviewToProduct(Integer reviewId, Review review) throws ReviewException {
//		Review existingReview = reviewRepository.findById(reviewId)
//				.orElseThrow(() -> new ReviewException("Review With Id "+reviewId+"Not Found In DataBase"));
//
//		existingReview.setComment(review.getComment());
//		existingReview.setRating(review.getRating());
//		return existingReview;
//	}
//
//	@Override
//	public void deleteReview(Integer reviewId) throws ReviewException {
//		Review existingReview = reviewRepository.findById(reviewId)
//				.orElseThrow(() -> new ReviewException("Review With Id "+reviewId+"Not Found In DataBase"));
//
//		reviewRepository.delete(existingReview);
//
//	}
//
//	@Override
//	public List<Review> getAllReviewOfProduct(Integer productId) throws ReviewException {
//		Product existingProduct = productRepository.findById(productId)
//				.orElseThrow(() -> new ReviewException("Invalid Product id"));
//
//		List<Review> allReviewsByProductId = reviewRepository.findAllReviewsByProductId(productId);
//		if(allReviewsByProductId.isEmpty()) {
//			 throw new ReviewException ("No Rewiew Of Given Product is Available");
//		}
//		return allReviewsByProductId;
//	}
//
//}
