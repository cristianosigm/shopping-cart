package cs.home.shopping.service;

import cs.home.shopping.dto.CartDTO;
import cs.home.shopping.dto.CartItemDTO;
import cs.home.shopping.model.entity.Cart;
import cs.home.shopping.model.entity.CartItem;
import cs.home.shopping.model.entity.Product;
import cs.home.shopping.model.entity.Promotion;
import cs.home.shopping.model.mapper.CartMapper;
import cs.home.shopping.model.mapper.PromotionMapper;
import cs.home.shopping.model.repository.CartItemRepository;
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

    private final CartItemRepository cartItemRepository;
    private final CartMapper cartMapper;
    private final CartRepository cartRepository;
    private final PromotionMapper promotionMapper;
    private final PromotionRepository promotionRepository;
    private final ProductRepository productRepository;

    @Autowired
    public CartService(CartMapper cartMapper, CartRepository cartRepository, CartItemRepository cartItemRepository, PromotionMapper promotionMapper, PromotionRepository promotionRepository, ProductRepository productRepository) {
        this.cartItemRepository = cartItemRepository;
        this.cartMapper = cartMapper;
        this.cartRepository = cartRepository;
        this.promotionMapper = promotionMapper;
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
            log.info(" >>> loading active promotions where requiresVIP is {}", cart.getCustomerIsVIP());
            final List<Promotion> applicablePromotions = this.promotionRepository.findAllByActiveTrueAndRequiresVIP(cart.getCustomerIsVIP());
            log.info(" >>> loaded {} promotions.", applicablePromotions.size());

            // Filtering active promotion for items
            final Promotion itemPromotion = applicablePromotions
                    .stream()
                    .filter(pr -> pr.getMinimumQuantity() > 0)
                    .findFirst().orElse(null);

            // Calculating the discount per item
            if (itemPromotion != null) {
                cart.getItems()
                        .stream()
                        .filter(cartItem -> cartItem.getQuantity() >= itemPromotion.getMinimumQuantity())
                        .forEach(cartItem -> cartItem.setDiscount(cartItem.getProduct().getPrice().multiply(
                                BigDecimal.valueOf(Math.ceil(cartItem.getQuantity() / itemPromotion.getMinimumQuantity())))));
            }

            // Parsing the CartDTO
            final CartDTO cartDTO = cartMapper.mapToDTO(cart);

            // Adding the list of applicable promotions
            cartDTO.setApplicablePromotions(promotionMapper.mapToDTO(applicablePromotions));

            // Calculating the total price
            cartDTO.setTotalPrice(cartDTO.getItems()
                    .stream()
                    .map(item -> item.getProduct().getPrice())
                    .reduce(BigDecimal.ZERO, BigDecimal::add));

            final BigDecimal itemDiscount = cartDTO.getItems()
                    .stream()
                    .map(CartItemDTO::getDiscount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            // Loading active promotion for VIPs and calculating
            if (cart.getCustomerIsVIP()) {
                final Promotion vipPromotion = applicablePromotions
                        .stream()
                        .filter(pr -> pr.getRequiresVIP())
                        .findFirst().orElse(null);

                if (vipPromotion != null
                        && vipPromotion.getDiscountPercent() != null
                        && vipPromotion.getDiscountPercent().compareTo(BigDecimal.ZERO) > 0) {
                    // Deciding which discount would be better
                    final BigDecimal vipDiscount = cartDTO.getTotalPrice().multiply(
                            vipPromotion.getDiscountPercent().divide(
                                    BigDecimal.valueOf(100)));

                    if (itemDiscount.compareTo(vipDiscount) > 0) {
                        cartDTO.setTotalDiscount(itemDiscount);
                    } else {
                        cartDTO.setTotalDiscount(vipDiscount);
                    }
                }
            } else {
                cartDTO.setTotalDiscount(itemDiscount);
            }
            return cartDTO;
        }
        return cartMapper.mapToDTO(cart);
        // @formatter:on
    }

    public void checkout(Long customerId) {

    }

}
