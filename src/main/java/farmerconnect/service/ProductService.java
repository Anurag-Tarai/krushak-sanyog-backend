package farmerconnect.service;

import java.io.IOException;
import java.util.List;

import farmerconnect.exception.ProductException;
import farmerconnect.exception.ProductNotFoundException;
import farmerconnect.model.Product;
import farmerconnect.dto.ProductDTO;
import org.springframework.web.multipart.MultipartFile;

public interface ProductService {

    Product addProduct(ProductDTO dto, MultipartFile[] images) throws IOException;

    Product uploadMoreImages(Integer productId, MultipartFile[] images) throws IOException;
    Product deleteSingleImage(Integer productId, String imageUrl);

    Product updateProduct(Integer productId, ProductDTO product);

	List<Product> getAllProduct(String keyword, String sortDirection, String sortBy);
	
	List<Product> getProductByCategory(String category);
	
	void removeProduct(Integer productId);

	Product getProductById(Integer productId);

    Product updateProductQuantity(Integer productId, Double quantity);

    List<Product> getByFarmerId();
}
 