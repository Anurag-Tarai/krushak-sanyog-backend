package farmerconnect.controller;

import java.io.IOException;
import java.util.List;

import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import farmerconnect.model.Product;
import farmerconnect.dto.ProductDTO;
import farmerconnect.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@Slf4j
public class ProductController {

    private final ProductService productService;

    /**
     * Create a new product
     * POST /api/v1/products
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Product> addProduct(
            @RequestPart("product") ProductDTO productDTO,
            @RequestPart("images") MultipartFile[] images
    ) throws IOException {
        Product product = productService.addProduct(productDTO, images);
        return new ResponseEntity<>(product, HttpStatus.CREATED);
    }

    @GetMapping("/farmer/{farmerId}")
    public ResponseEntity<?> getProductsByFarmerId(@PathVariable @Positive Integer farmerId) {
        log.info("Fetching products for farmerId: {}", farmerId);

        List<Product> products = productService.getByFarmerId(farmerId);

        return products.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(products);
    }

    /**
     * Upload more images to a product (max total 5)
     */
    @PostMapping(value = "/{productId}/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Product> uploadMoreImages(
            @PathVariable Integer productId,
            @RequestPart("images") MultipartFile[] images
    ) throws IOException {
        Product updatedProduct = productService.uploadMoreImages(productId, images);
        return ResponseEntity.ok(updatedProduct);
    }

    /**
     * Delete a single image from a product
     */
    @DeleteMapping("/{productId}/images")
    public ResponseEntity<Product> deleteSingleImage(
            @PathVariable Integer productId,
            @RequestParam String imageUrl
    ) {
        Product updatedProduct = productService.deleteSingleImage(productId, imageUrl);
        return ResponseEntity.ok(updatedProduct);
    }

    /**
     * Update entire product
     * PUT /api/v1/products/{productId}
     */
    @PutMapping("/{productId}")
    public ResponseEntity<?> updateProduct(
            @PathVariable Integer productId,
            @Valid @RequestBody ProductDTO updatedProduct) {
            Product updatedProductResult = productService.updateProduct(productId, updatedProduct);
            return ResponseEntity.ok(updatedProductResult);
    }

    /**
     * Update only product price
     * PATCH /api/v1/products/{productId}/price
     */
    @PatchMapping("/{productId}/quantity")
    public ResponseEntity<Product> updateProductPrice(
            @PathVariable Integer productId,
            @RequestParam Double quantity) {
        Product updatedProduct = productService.updateProductQuantity(productId, quantity);
        return ResponseEntity.ok(updatedProduct);
    }

    /**
     * Get all products or search by keyword
     * GET /api/v1/products?keyword=&sort=&sortBy=
     */
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false, defaultValue = "asc") String sort,
            @RequestParam(required = false, defaultValue = "quantity") String sortBy) {
        List<Product> products = productService.getAllProduct(keyword, sort, sortBy);
        return ResponseEntity.ok(products);
    }

    /**
     * Get product by ID
     * GET /api/v1/products/{productId}
     */
    @GetMapping("/{productId}")
    public ResponseEntity<Product> getSingleProduct(@PathVariable Integer productId) {
        Product singleProduct = productService.getProductById(productId);
        return ResponseEntity.ok(singleProduct);
    }


    /**
     * Get products by category
     * GET /api/v1/products/category/{category}
     */
    @GetMapping("/category/{category}")
    public ResponseEntity<List<Product>> getProductByCategory(@PathVariable String category) {
        List<Product> products = productService.getProductByCategory(category);
        return ResponseEntity.ok(products);
    }

    /**
     * Delete product
     * DELETE /api/v1/products/{productId}
     */
    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> removeProduct(@PathVariable Integer productId) {
        productService.removeProduct(productId);
        return ResponseEntity.noContent().build();
    }
}
