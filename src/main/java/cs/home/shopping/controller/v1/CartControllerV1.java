package cs.home.shopping.controller.v1;

import cs.home.shopping.api.v1.CartResourceV1;
import cs.home.shopping.dto.CartDTO;
import cs.home.shopping.dto.response.SuccessResponseDTO;
import cs.home.shopping.service.CartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class CartControllerV1 implements CartResourceV1 {

    private final CartService cartService;

    @Autowired
    public CartControllerV1(CartService cartService) {
        this.cartService = cartService;
    }

    @Override
    public ResponseEntity<SuccessResponseDTO> addProduct(
            @RequestHeader("customerId") Long customerId,
            @PathVariable("productId") Long productId,
            @PathVariable("quantity") Integer quantity) {
        this.cartService.addProduct(customerId, productId, quantity);
        return ResponseEntity.ok(SuccessResponseDTO.builder().message("Product added.").build());
    }

    @Override
    public ResponseEntity<SuccessResponseDTO> removeProduct(Long customerId, Long productId) {
        this.cartService.removeProduct(customerId, productId);
        return ResponseEntity.ok(SuccessResponseDTO.builder().message("Product removed.").build());
    }

    @Override
    public ResponseEntity<SuccessResponseDTO> clearCart(Long customerId) {
        this.cartService.clearCart(customerId);
        return ResponseEntity.ok(SuccessResponseDTO.builder().message("Cart successfully cleared.").build());
    }

    @Override
    public ResponseEntity<CartDTO> loadCart(@RequestHeader("customerId") Long customerId) {
        log.info("CustomerId: {}", customerId);
        return ResponseEntity.ok(this.cartService.loadCart(customerId));
    }

    @Override
    public ResponseEntity<SuccessResponseDTO> checkout(Long customerId) {
        return ResponseEntity.ok(SuccessResponseDTO.builder().message("Checkout operation successful.").build());
    }
}
