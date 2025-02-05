package cs.home.shopping.service;

import cs.home.shopping.dto.CartDTO;
import cs.home.shopping.model.entity.Cart;
import cs.home.shopping.model.entity.CartItem;
import cs.home.shopping.model.entity.Product;
import cs.home.shopping.model.entity.Promotion;
import cs.home.shopping.model.mapper.CartMapper;
import cs.home.shopping.model.mapper.PromotionMapper;
import cs.home.shopping.model.repository.CartRepository;
import cs.home.shopping.model.repository.ProductRepository;
import cs.home.shopping.model.repository.PromotionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class CartService {

    // TODO: use parallelStream wherever possible

    private final CartMapper cartMapper;
    private final CartRepository cartRepository;
    private final PromotionMapper promotionMapper;
    private final PromotionService promotionService;
    private final PromotionRepository promotionRepository;
    private final ProductRepository productRepository;

    @Autowired
    public CartService(CartMapper cartMapper, CartRepository cartRepository, PromotionMapper promotionMapper, PromotionService promotionService, PromotionRepository promotionRepository, ProductRepository productRepository) {
        this.cartMapper = cartMapper;
        this.cartRepository = cartRepository;
        this.promotionMapper = promotionMapper;
        this.promotionService = promotionService;
        this.promotionRepository = promotionRepository;
        this.productRepository = productRepository;
    }

    public void addProduct(Long customerId, Long productId, Integer quantity) {
        final Cart cart = this.cartRepository.findByCustomerId(customerId).orElse(null);
        // @formatter:off
        if (cart != null) {
            final CartItem item = cart.getItems().stream()
                    .filter(it -> it.getProduct().getId() == productId)
                    .findFirst()
                    .orElse(null);
            if(item != null) {
                log.info(" >> Item found, increasing quantity");
                item.setQuantity(item.getQuantity() + quantity);
            } else {
                log.info(" >> Item not found, adding");
                final Product product = this.productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Item not found."));
                cart.getItems().add(CartItem.builder()
                        .product(product)
                        .quantity(quantity)
                        .build());
            }
            this.cartRepository.save(cart);
        } else {
            final Product product = this.productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Item not found."));
            this.cartRepository.save(Cart.builder()
                    .customerId(customerId)
                    .items(Collections.singletonList(
                            CartItem.builder()
                                    .product(product)
                                    .quantity(quantity)
                                    .build())
                    ).build());
        }
        // @formatter:on
    }

    public void removeProduct(Long customerId, Long productId) {
        final Cart cart = this.cartRepository.findByCustomerId(customerId).orElseThrow(() -> new RuntimeException("Customer has no cart"));
        cart.getItems().removeIf(it -> it.getProduct().getId() == productId);
        this.cartRepository.save(cart);
    }

    public void clearCart(Long customerId) {
        final Cart cart = this.cartRepository.findByCustomerId(customerId).orElseThrow(() -> new RuntimeException("Customer has no cart."));
        cart.setItems(new ArrayList<>());
        // TODO update cart with empty list
    }

    public CartDTO loadCart(Long customerId) {
        // @formatter:off
        final Cart cart = this.cartRepository.findByCustomerId(customerId).orElse(Cart.builder().customerId(customerId).build());

        if (cart.getItems().size() > 0) {
            // Loading applicable promotions
            final List<Promotion> applicablePromotions = this.promotionRepository.findAllByActiveTrueAndRequiresVIP(cart.getCustomerIsVIP());

            // Calculating the total price
            final BigDecimal totalPrice = cart.getItems()
                    .stream()
                    .map(item -> item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            // Calculating the total item's discount. Can be zero if no rules matched.
            final BigDecimal itemDiscount = promotionService.calculateDiscountBasedOnItems(cart.getItems(), applicablePromotions);

            // Loading active promotion for VIPs and calculating
            final BigDecimal vipDiscount = promotionService.calculateDiscountForVIP(
                    cart.getCustomerIsVIP(), totalPrice, applicablePromotions);

            // Parsing the CartDTO and adding the results
            final CartDTO cartDTO = cartMapper.mapToDTO(cart);
            cartDTO.setTotalPrice(totalPrice);
            log.info(" >>>> RESULTS: vipDiscount = {}, itemDiscount = {}; totalPrice = {}", vipDiscount, itemDiscount, totalPrice);
            cartDTO.setTotalDiscount(vipDiscount.compareTo(itemDiscount) > 0 ? vipDiscount : itemDiscount);
            cartDTO.setFinalPrice(cartDTO.getTotalPrice().subtract(cartDTO.getTotalDiscount()));

            return cartDTO;
        }
        return cartMapper.mapToDTO(cart);
        // @formatter:on
    }

    public void checkout(Long customerId) {

    }

}
