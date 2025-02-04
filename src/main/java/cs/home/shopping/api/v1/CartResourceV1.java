package cs.home.shopping.api.v1;

import cs.home.shopping.dto.CartDTO;
import cs.home.shopping.dto.response.SuccessResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/cart")
public interface CartResourceV1 {

    @PostMapping("/{id}/product/{productId}/quantity/{quantity}")
    ResponseEntity<SuccessResponseDTO> addProduct(Long customerId, Long productId, Integer quantity);

    @DeleteMapping("/{id}/product/{productId}")
    ResponseEntity<SuccessResponseDTO> removeProduct(Long customerId, Long productId);

    @DeleteMapping("/{id}")
    ResponseEntity<SuccessResponseDTO> clearCart(Long customerId);

    @GetMapping("/{id}")
    ResponseEntity<CartDTO> loadCart(Long customerId);

    @PutMapping("/{id}/checkout")
    ResponseEntity<SuccessResponseDTO> checkout(Long customerId);

}
