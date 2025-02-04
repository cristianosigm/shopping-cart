package cs.home.shopping.service;

import cs.home.shopping.dto.CartDTO;
import cs.home.shopping.model.entity.Cart;
import cs.home.shopping.model.entity.CartItem;
import cs.home.shopping.model.entity.Product;
import cs.home.shopping.model.mapper.CartMapper;
import cs.home.shopping.model.repository.CartItemRepository;
import cs.home.shopping.model.repository.CartRepository;
import cs.home.shopping.model.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Slf4j
@Service
public class CartService {

    private final CartMapper cartMapper;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final PromotionService promotionService;
    private final ProductRepository productRepository;

    @Autowired
    public CartService(CartMapper cartMapper, CartRepository cartRepository, CartItemRepository cartItemRepository, PromotionService promotionService, ProductRepository productRepository) {
        this.cartMapper = cartMapper;
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.promotionService = promotionService;
        this.productRepository = productRepository;
    }

    public void addProduct(Long customerId, Long productId, Integer quantity) {
        final Product product = this.productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Item not found."));
        final Cart cart = this.cartRepository.findByCustomerId(customerId).orElse(null);
        // @formatter:off
        if (cart != null) {
            final CartItem item = cart.getItems().stream()
                    .filter(it -> it.getProductId() == productId)
                    .findFirst()
                    .orElse(null);
            if(item != null) {
                log.info(" >> Item found, increasing quantity");
                item.setQuantity(item.getQuantity() + quantity);
            } else {
                log.info(" >> Item not found, adding");
                cart.getItems().add(CartItem.builder()
                        .productId(productId)
                        .productName(product.getName())
                        .productDescription(product.getDescription())
                        .quantity(quantity)
                        .unitPrice(product.getPrice())
                        .build());
            }
            this.cartRepository.save(cart);
        } else {
            this.cartRepository.save(Cart.builder()
                    .customerId(customerId)
                    .items(Arrays.asList(
                            CartItem.builder()
                                    .productId(productId)
                                    .productName(product.getName())
                                    .productDescription(product.getDescription())
                                    .quantity(quantity)
                                    .unitPrice(product.getPrice())
                                    .build())
                    ).build());
        }
        // @formatter:on
    }

    public void removeProduct(Long customerId, Long productId) {
        final Cart cart = this.cartRepository.findByCustomerId(customerId).orElseThrow(() -> new RuntimeException("Customer has no cart"));
        cart.getItems().removeIf(it -> it.getProductId() == productId);
        this.cartRepository.save(cart);
    }

    public void clearCart(Long customerId) {
        final Cart cart = this.cartRepository.findByCustomerId(customerId).orElseThrow(() -> new RuntimeException("Customer has no cart."));
    }

    public CartDTO loadCart(Long customerId) {
        return cartMapper.mapToDTO(this.cartRepository.findByCustomerId(customerId).orElse(Cart.builder().customerId(customerId).build()));
    }

    public void checkout(Long customerId) {

    }

}
