package farmerconnect.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import farmerconnect.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import farmerconnect.exception.ProductException;
import farmerconnect.model.Product;
import farmerconnect.dto.ProductDTO;
import farmerconnect.repository.ProductRepository;
import farmerconnect.service.ProductService;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;


@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final S3Service s3Service;

    @Override
    public Product addProduct(ProductDTO dto, MultipartFile[] images) throws IOException {
        if (images == null || images.length == 0) {
            throw new IllegalArgumentException("At least 1 image is required");
        }

// Check if all files are empty
        boolean hasValidFile = Arrays.stream(images)
                .anyMatch(file -> file != null && !file.isEmpty());

        if (!hasValidFile) {
            throw new IllegalArgumentException("At least 1 non-empty image is required");
        }

        if (images.length > 5) {
            throw new IllegalArgumentException("Maximum 5 images allowed");
        }


        Product product = new Product();
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setCategory(dto.getCategory());
        product.setQuantity(dto.getQuantity());
        product.setAvailable(dto.isAvailable());
        product.setLatitude(dto.getLatitude());
        product.setLongitude(dto.getLongitude());
        product.setAddress(dto.getAddress());
        product.setFarmerId(dto.getFarmerId());
        product.setVariants(dto.getVariants());

        List<String> imageUrls = new ArrayList<>();
        for (MultipartFile file : images) {
            if (file != null && !file.isEmpty()) {
                String imageUrl = s3Service.uploadFile(file, "products");
                imageUrls.add(imageUrl);
            }
        }
        product.setImageUrls(imageUrls);

        return productRepository.save(product);
    }


    @Override
    public Product updateProduct(Integer productId, ProductDTO updatedProduct) throws ProductException {

        Optional<Product> product = productRepository.findById(productId);
        if (product.isEmpty()) {
            throw new ProductException("Product with ID " + productId + " not found.");
        }
        Product existingProduct = product.get();

        // Update the existing product's properties with the new data
        existingProduct.setName(updatedProduct.getName());
        existingProduct.setCategory(updatedProduct.getCategory());
        existingProduct.setQuantity(updatedProduct.getQuantity());
        existingProduct.setDescription(updatedProduct.getDescription());

        // Handling the variant field
        if (updatedProduct.getVariants() != null) {
            // If variants are provided, set them to the product
            existingProduct.setVariants(updatedProduct.getVariants().toString());
        } else {
            // Optionally, you can set variants to null or empty if no variants are provided
            existingProduct.setVariants(null);  // or existingProduct.setVariants(new ArrayList<>());
        }

        productRepository.save(existingProduct);
        return existingProduct;
    }

    @Override
    public List<Product> getProductByName(String name) throws ProductException {

        List<Product> existProductByName = productRepository.findByName(name);
        if (existProductByName.isEmpty()) {
            throw new ProductException("Product Not found with name " + name);
        }
        return existProductByName;
    }

    @Override
    public List<Product> getAllProduct(String keyword, String sortDirection, String sortBy) throws ProductException {

        Sort sort = Sort.by(sortDirection.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC,sortBy);

        List<Product> products;

        if (keyword != null) {

            products = productRepository.findAllByNameContainingIgnoreCase(keyword, sort);
        } else {
            products = productRepository.findAll(sort);
        }
        if (products.isEmpty()) {
            throw new ProductException("Product List Empty");
        }

        return products;

    }

    @Override
    public List<Product> getProductByCategory(String category) throws ProductException {
        // Retrieve products by category from the database
        List<Product> allproductCategoryName = productRepository.getProductCategoryName(category);
        if (allproductCategoryName.isEmpty())
            throw new ProductException("Product with category Name " + category + " not found.");

        return allproductCategoryName;
    }


    @Override
    public void removeProduct(Integer productId) throws ProductException {

        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ProductException("Product with ID " + productId + " not found."));

        productRepository.delete(existingProduct);
    }

    @Override
    public Product getSingleProduct(Integer productId) {

        return productRepository.findById(productId).orElseThrow(() -> new ProductException("Product not found"));
    }

    @Override
    public Product updateProductPrice(Integer productId, Double newPrice) {
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));

        existingProduct.setQuantity(newPrice);
        return productRepository.save(existingProduct);
    }
}
