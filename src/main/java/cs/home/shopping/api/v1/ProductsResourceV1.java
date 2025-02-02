package cs.home.shopping.api.v1;

import cs.home.shopping.api.v1.common.DeleteResourceV1;
import cs.home.shopping.api.v1.common.ReadResourceV1;
import cs.home.shopping.dto.ProductCreateDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("api/v1/products")
public interface ProductsResourceV1 extends ReadResourceV1, DeleteResourceV1 {

    @PostMapping
    ResponseEntity<?> addAll(@RequestBody @Valid List<ProductCreateDTO> items);
}
