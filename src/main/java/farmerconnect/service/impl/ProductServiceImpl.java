package farmerconnect.service.impl;

import java.io.IOException;
import java.util.*;

import farmerconnect.exception.*;
import farmerconnect.model.User;
import farmerconnect.security.CustomUserDetails;
import farmerconnect.service.S3Service;
import farmerconnect.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

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
    private final UserService userService;

    @Override
    public Product addProduct(ProductDTO dto, MultipartFile[] images) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Integer farmerId = userDetails.getUserId();


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
        product.setLatitude(dto.getLatitude());
        product.setLongitude(dto.getLongitude());
        product.setAddress(dto.getAddress());
        product.setFarmerId(farmerId);
        product.setVariants(dto.getVariants());
        product = productRepository.save(product); // save first to get productId

        List<String> imageUrls = new ArrayList<>();
        for (MultipartFile file : images) {
            if (file != null && !file.isEmpty()) {
                String imageUrl = s3Service.uploadFile(file, "products", product.getProductId());
                imageUrls.add(imageUrl);
            }
        }
        product.setImageUrls(imageUrls);
        return productRepository.save(product);
    }

    @Override
    public Product uploadMoreImages(Integer productId, MultipartFile[] images) throws IOException {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with ID: " + productId));

        List<String> existingImages = new ArrayList<>(product.getImageUrls());

        if (existingImages.size() >= 5 || existingImages.size()+images.length>5) {
            throw new ImageLimitExceedException("Cannot upload more images. Limit of 5 reached.");
        }

        for (MultipartFile file : images) {
            if (existingImages.size() >= 5) break;
            if (file != null && !file.isEmpty()) {
                String imageUrl = s3Service.uploadFile(file, "products",  productId);
                existingImages.add(imageUrl);
            }
        }
        product.setImageUrls(existingImages);
        return productRepository.save(product);
    }

    @Override
    public Product deleteSingleImage(Integer productId, String imageUrl) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with ID: " + productId));

        List<String> updatedImages = new ArrayList<>(product.getImageUrls());

        if (!updatedImages.remove(imageUrl)) {
            throw new ImageNotFoundException("Image not found in this product");
        }

        // Delete from S3
        s3Service.deleteFile(imageUrl);

        product.setImageUrls(updatedImages);
        return productRepository.save(product);
    }

    @Override
    public Product updateProduct(Integer productId, ProductDTO updatedProduct){

        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product with ID " + productId + " not found."));

        // Update fields only if they are not null
        if (updatedProduct.getName() != null) {
            existingProduct.setName(updatedProduct.getName());
        }

        if (updatedProduct.getDescription() != null) {
            existingProduct.setDescription(updatedProduct.getDescription());
        }

        if (updatedProduct.getCategory() != null) {
            existingProduct.setCategory(updatedProduct.getCategory());
        }

        if (updatedProduct.getQuantity() != null) {
            existingProduct.setQuantity(updatedProduct.getQuantity());
        }

        if (updatedProduct.getLatitude() != null) {
            existingProduct.setLatitude(updatedProduct.getLatitude());
        }

        if (updatedProduct.getLongitude() != null) {
            existingProduct.setLongitude(updatedProduct.getLongitude());
        }

        if (updatedProduct.getAddress() != null) {
            existingProduct.setAddress(updatedProduct.getAddress());
        }


        if (updatedProduct.getVariants() != null) {
            existingProduct.setVariants(updatedProduct.getVariants());
        }

        return productRepository.save(existingProduct);
    }

    @Override
    public List<Product> getAllProduct(String keyword, String sortDirection, String sortBy){

        Sort sort = Sort.by(sortDirection.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC,sortBy);

        List<Product> products;

        if (keyword != null) {

            products = productRepository.findAllByNameContainingIgnoreCase(keyword, sort);
        } else {
            products = productRepository.findAll(sort);
        }
        return products;
    }

    @Override
    public List<Product> getProductByCategory(String category) {
        // Retrieve products by category from the database
        List<Product> allproductCategoryName = productRepository.getProductCategoryName(category);
        if (allproductCategoryName.isEmpty())
            throw new ProductException("Product with category Name " + category + " not found.");

        return allproductCategoryName;
    }


    @Override
    public void removeProduct(Integer productId) {

        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product with ID " + productId + " not found."));
        s3Service.deleteAllFiles("products", Long.valueOf(productId));
        productRepository.delete(existingProduct);
    }

        @Override
        public Product getProductById(Integer productId) {
            return productRepository.findById(productId).orElseThrow(() ->
                    new ProductNotFoundException("Product not found with id : "+productId));
        }

    @Override
    public Product updateProductQuantity(Integer productId, Double quantity) {
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));

        existingProduct.setQuantity(quantity);
        return productRepository.save(existingProduct);
    }

    @Override
    public List<Product> getByFarmerId() {
        CustomUserDetails authUserDetails = userService.getCurrentAuthUser();
        Integer userId = authUserDetails.getUserId();
        if(!authUserDetails.getRole().equals("ROLE_FARMER"))
            throw new InvalidCredentialException("You are not farmer");
        return productRepository.findAllByFarmerId(userId);
    }
}
