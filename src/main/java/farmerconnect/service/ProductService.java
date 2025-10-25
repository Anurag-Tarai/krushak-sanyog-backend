package farmerconnect.service;

import java.io.IOException;
import java.util.List;

import farmerconnect.exception.ProductException;
import farmerconnect.model.Product;
import farmerconnect.dto.ProductDTO;
import org.springframework.web.multipart.MultipartFile;

public interface ProductService {

    Product addProduct(ProductDTO dto, MultipartFile[] images) throws IOException;

    public Product updateProduct(Integer productId, ProductDTO product)throws ProductException;
	
	public List<Product> getProductByName(String name)throws ProductException;
	
	public List<Product> getAllProduct(String keyword, String sortDirection, String sortBy)throws ProductException;
	
	public List<Product> getProductByCategory(String catagory) throws ProductException;
	
	public void removeProduct(Integer productId)throws ProductException;

	public Product getSingleProduct(Integer productId);

    Product updateProductPrice(Integer productId, Double newPrice);
}
 