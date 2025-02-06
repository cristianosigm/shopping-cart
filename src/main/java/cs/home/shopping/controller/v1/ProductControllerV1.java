package cs.home.shopping.controller.v1;

import cs.home.shopping.api.v1.ProductResourceV1;
import cs.home.shopping.dto.ProductDTO;
import cs.home.shopping.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProductControllerV1 implements ProductResourceV1 {

    private final ProductService productService;

    @Autowired
    public ProductControllerV1(ProductService productService) {
        this.productService = productService;
    }

    @Override
    public ResponseEntity<List<ProductDTO>> findAll() {
        return ResponseEntity.ok(this.productService.findAll());
    }

    @Override
    public ResponseEntity<ProductDTO> findById(Long id) {
        return ResponseEntity.ok(productService.findById(id));
    }

    @Override
    public ResponseEntity<List<ProductDTO>> addAll(@RequestBody @Valid List<ProductDTO> items) {
        return ResponseEntity.ok(this.productService.addAll(items));
    }
}
