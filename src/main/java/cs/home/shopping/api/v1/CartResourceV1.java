package cs.home.shopping.api.v1;

import cs.home.shopping.dto.CartDTO;
import cs.home.shopping.dto.response.SuccessResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/cart")
public interface CartResourceV1 {

    @PostMapping("/product/{productId}/quantity/{quantity}")
    ResponseEntity<SuccessResponseDTO> addProduct(@RequestHeader("customerId") Long customerId,
                                                  @PathVariable("productId") Long productId,
                                                  @PathVariable("quantity") Integer quantity);

    @DeleteMapping("/product/{productId}")
    ResponseEntity<SuccessResponseDTO> removeProduct(@RequestHeader("customerId") Long customerId,
                                                     @PathVariable("productId") Long productId);

    @DeleteMapping
    ResponseEntity<SuccessResponseDTO> clearCart(@RequestHeader("customerId") Long customerId);

    @GetMapping
    ResponseEntity<CartDTO> loadCart(@RequestHeader("customerId") Long customerId);

    @PutMapping("/checkout")
    ResponseEntity<SuccessResponseDTO> checkout(@RequestHeader("customerId") Long customerId);
}
