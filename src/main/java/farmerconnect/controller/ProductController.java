package farmerconnect.controller;

import java.io.IOException;
import java.util.List;

import lombok.RequiredArgsConstructor;
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

    /**
     * Update entire product
     * PUT /api/v1/products/{productId}
     */
    @PutMapping("/{productId}")
    public ResponseEntity<Product> updateProduct(
            @PathVariable Integer productId,
            @Valid @RequestBody ProductDTO updatedProduct) {
        Product updatedProductResult = productService.updateProduct(productId, updatedProduct);
        return ResponseEntity.ok(updatedProductResult);
    }

    /**
     * Update only product price
     * PATCH /api/v1/products/{productId}/price
     */
    @PatchMapping("/{productId}/price")
    public ResponseEntity<Product> updateProductPrice(
            @PathVariable Integer productId,
            @RequestBody Double newPrice) {
        Product updatedProduct = productService.updateProductPrice(productId, newPrice);
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
            @RequestParam(required = false, defaultValue = "price") String sortBy) {
        List<Product> products = productService.getAllProduct(keyword, sort, sortBy);
        return ResponseEntity.ok(products);
    }

    /**
     * Get product by ID
     * GET /api/v1/products/{productId}
     */
    @GetMapping("/{productId}")
    public ResponseEntity<Product> getSingleProduct(@PathVariable Integer productId) {
        Product singleProduct = productService.getSingleProduct(productId);
        return ResponseEntity.ok(singleProduct);
    }

    /**
     * Get products by name
     * GET /api/v1/products/search?name=
     */
    @GetMapping("/search")
    public ResponseEntity<List<Product>> getProductByName(@RequestParam String name) {
        List<Product> products = productService.getProductByName(name);
        return ResponseEntity.ok(products);
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
