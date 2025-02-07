package cs.home.shopping.service;

import cs.home.shopping.dto.CartDTO;
import cs.home.shopping.exception.InvalidOperationException;
import cs.home.shopping.exception.ItemNotFoundException;
import cs.home.shopping.model.entity.*;
import cs.home.shopping.model.repository.CartItemRepository;
import cs.home.shopping.model.repository.CartRepository;
import cs.home.shopping.model.repository.OrderRepository;
import cs.home.shopping.model.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final PromotionService promotionService;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final ModelMapper mapper;

    @Autowired
    public CartService(CartRepository cartRepository, CartItemRepository cartItemRepository,
                       PromotionService promotionService, ProductRepository productRepository,
                       OrderRepository orderRepository, ModelMapper mapper) {
        this.mapper = mapper;
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.promotionService = promotionService;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
    }

    public CartDTO addProduct(Long customerId, Long productId, Integer quantity) {
        log.info("Adding productId {} (quantity: {}) on current cart", productId, quantity);
        final Cart cart = cartRepository.findByCustomerId(customerId)
            .orElse(null);

        if (cart != null) {
            final CartItem item = cart.getItems()
                .stream()
                .filter(cartItem -> cartItem.getProduct()
                    .getId()
                    .equals(productId))
                .findFirst()
                .orElse(null);
            if (item != null) {
                log.info("Item found, increasing quantity");
                item.setQuantity(item.getQuantity() + quantity);
            } else {
                log.info("Item not found, adding it");
                final Product product = productRepository.findById(productId)
                    .orElseThrow(ItemNotFoundException::new);
                cart.getItems()
                    .add(CartItem.builder()
                        .product(product)
                        .quantity(quantity)
                        .build());
            }
            return mapper.map(cartRepository.save(cart), CartDTO.class);
        } else {
            final Product product = productRepository.findById(productId)
                .orElseThrow(ItemNotFoundException::new);
            return mapper.map(cartRepository.save(Cart.builder()
                .customerId(customerId)
                .items(Collections.singletonList(CartItem.builder()
                    .product(product)
                    .quantity(quantity)
                    .build()))
                .build()), CartDTO.class);
        }
    }

    @Transactional
    public void removeProduct(Long customerId, Long productId) {
        final Cart cart = cartRepository.findByCustomerId(customerId)
            .orElseThrow(() -> new ItemNotFoundException("Customer has no cart"));
        cart.getItems()
            .removeIf(it -> it.getProduct()
                .getId()
                .equals(productId));
        cartRepository.saveAndFlush(cart);
    }

    public CartDTO loadCart(Long customerId) {
        final Cart cart = cartRepository.findByCustomerId(customerId)
            .orElse(Cart.builder()
                .customerId(customerId)
                .build());

        if (!cart.getItems()
            .isEmpty()) {
            // Loading applicable promotions
            final List<Promotion> applicablePromotions = promotionService.findAllActiveForVipStatus(
                cart.getCustomerIsVIP());

            // Calculating the total price
            final BigDecimal totalPrice = cart.getItems()
                .stream()
                .map(item -> item.getProduct()
                    .getPrice()
                    .multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

            // Calculating the total item's discount. Can be zero if no rules matched.
            final BigDecimal itemDiscount = promotionService.calculateDiscountBasedOnItems(cart.getItems(),
                applicablePromotions);

            // Loading active promotion for VIPs and calculating
            final BigDecimal vipDiscount = promotionService.calculateDiscountForVIP(cart.getCustomerIsVIP(), totalPrice,
                applicablePromotions);

            // Parsing the CartDTO and adding the results
            final CartDTO cartDTO = mapper.map(cart, CartDTO.class);
            cartDTO.setTotalPrice(totalPrice);
            cartDTO.setTotalDiscount(vipDiscount.compareTo(itemDiscount) > 0 ? vipDiscount : itemDiscount);
            cartDTO.setFinalPrice(cartDTO.getTotalPrice()
                .subtract(cartDTO.getTotalDiscount()));

            return cartDTO;
        }
        return mapper.map(cart, CartDTO.class);
    }

    public void checkout(Long customerId) {
        // Checkout will be called to parse the current cart into an order.
        final CartDTO cart = loadCart(customerId);

        if (!cart.getItems()
            .isEmpty()) {
            orderRepository.save(Order.builder()
                .items(cart.getItems()
                    .stream()
                    .map(item -> OrderItem.builder()
                        .product(mapper.map(item.getProduct(), Product.class))
                        .quantity(item.getQuantity())
                        .build())
                    .toList())
                .build());
        } else {
            throw new InvalidOperationException("Cannot checkout an empty cart.");
        }
    }
}
