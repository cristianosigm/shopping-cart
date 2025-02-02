package cs.home.shopping.controller.v1;

import cs.home.shopping.api.v1.ProductsResourceV1;
import cs.home.shopping.dto.ProductCreateDTO;
import cs.home.shopping.dto.mapper.ProductMapper;
import cs.home.shopping.model.entity.Product;
import cs.home.shopping.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProductsControllerV1 implements ProductsResourceV1 {

    private final ProductService productService;
    private final ProductMapper productMapper;

    @Autowired
    public ProductsControllerV1(ProductService productService, ProductMapper productMapper) {
        this.productService = productService;
        this.productMapper = productMapper;
    }

    @Override
    public ResponseEntity<String> deleteById(Integer id) {
        return ResponseEntity.ok("deleted");
    }

    @Override
    public ResponseEntity<List<Product>> findAll() {
        return ResponseEntity.ok(this.productService.findAll());
    }

    @Override
    public ResponseEntity<String> findById(Integer id) {
        return ResponseEntity.ok("product by id: " + id);
    }

    @Override
    public ResponseEntity<List<Product>> addAll(@RequestBody @Valid List<ProductCreateDTO> items) {
        return ResponseEntity.ok(this.productService.addAll(this.productMapper.map(items)));
    }
}
