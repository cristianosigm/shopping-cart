package cs.home.shopping.service;

import cs.home.shopping.dto.CartDTO;
import cs.home.shopping.model.entity.*;
import cs.home.shopping.model.mapper.CartItemMapper;
import cs.home.shopping.model.mapper.CartMapper;
import cs.home.shopping.model.mapper.ProductMapper;
import cs.home.shopping.model.mapper.PromotionMapper;
import cs.home.shopping.model.repository.CartItemRepository;
import cs.home.shopping.model.repository.CartRepository;
import cs.home.shopping.model.repository.ProductRepository;
import cs.home.shopping.model.repository.PromotionRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
public class CartService {

    private final CartMapper cartMapper = new CartMapper(new ModelMapper());
    private final CartItemMapper cartItemMapper = new CartItemMapper(new ModelMapper());
    private final PromotionMapper promotionMapper = new PromotionMapper(new ModelMapper());
    private final ProductMapper productMapper = new ProductMapper(new ModelMapper());

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final PromotionService promotionService;
    private final PromotionRepository promotionRepository;
    private final ProductRepository productRepository;

    @Autowired
    public CartService(CartRepository cartRepository, CartItemRepository cartItemRepository,
                       PromotionService promotionService, PromotionRepository promotionRepository,
                       ProductRepository productRepository) {
        //        this.mapper = mapper;
        //        this.cartMapper = cartMapper;
        this.cartRepository = cartRepository;
        //        this.cartItemMapper = cartItemMapper;
        //        this.promotionMapper = promotionMapper;
        this.cartItemRepository = cartItemRepository;
        this.promotionService = promotionService;
        this.promotionRepository = promotionRepository;
        this.productRepository = productRepository;
    }

    public CartDTO addProduct(Long customerId, Long productId, Integer quantity) {
        log.info("Adding productId {} (quantity: {}) on cart with customerId {}", productId, quantity, customerId);
        final Cart cart = this.cartRepository.findByCustomerId(customerId)
            .orElse(null);

        if (cart != null) {
            final CartItem item = cart.getItems()
                .stream()
                .filter(cartItem -> cartItem.getProduct()
                    .getId() == productId)
                .findFirst()
                .orElse(null);
            if (item != null) {
                log.info("Item found, increasing quantity");
                item.setQuantity(item.getQuantity() + quantity);
            } else {
                log.info("Item not found, adding it");
                final Product product = this.productRepository.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Item not found."));
                cart.getItems()
                    .add(CartItem.builder()
                        .product(product)
                        .quantity(quantity)
                        .build());
            }
            return cartMapper.mapToDTO(cartRepository.save(cart)); //, CartDTO.class);
        } else {
            final Product product = this.productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Item not found."));
            return cartMapper.mapToDTO(this.cartRepository.save(Cart.builder()
                .customerId(customerId)
                .items(Arrays.asList(CartItem.builder()
                    .product(product)
                    .quantity(quantity)
                    .build()))
                .build())); //, CartDTO.class);
        }
    }

    public void removeProduct(Long customerId, Long productId) {
        final Cart cart = this.cartRepository.findByCustomerId(customerId)
            .orElseThrow(() -> new RuntimeException("Customer has no cart"));
        cart.getItems()
            .removeIf(it -> it.getProduct()
                .getId() == productId);
        this.cartRepository.save(cart);
    }

    @Transactional
    public void clearCart(Long customerId) {
        log.warn("Removing all items and values from customer's cart {}", customerId);
        final Cart cart = this.cartRepository.findByCustomerId(customerId)
            .orElseThrow(() -> new RuntimeException("Customer has no cart."));

        log.warn(" >>> items to delete: {}", cart.getItems()
            .size());

        cartItemRepository.deleteAll(cart.getItems());

        final int curItems = cartItemRepository.findAll()
            .size();

        log.warn("Remaining items: {}", curItems);
    }

    public CartDTO loadCart(Long customerId) {
        final Cart cart = this.cartRepository.findByCustomerId(customerId)
            .orElse(Cart.builder()
                .customerId(customerId)
                .build());

        if (cart.getItems()
            .size() > 0) {
            // Loading applicable promotions
            final List<Promotion> applicablePromotions = this.promotionRepository.findAllByActiveTrueAndRequiresVIP(
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
            final CartDTO cartDTO = cartMapper.mapToDTO(cart); //, CartDTO.class);
            cartDTO.setTotalPrice(totalPrice);
            cartDTO.setTotalDiscount(vipDiscount.compareTo(itemDiscount) > 0 ? vipDiscount : itemDiscount);
            cartDTO.setFinalPrice(cartDTO.getTotalPrice()
                .subtract(cartDTO.getTotalDiscount()));

            return cartDTO;
        }
        return cartMapper.mapToDTO(cart); //, CartDTO.class);
    }

    public void checkout(Long customerId) {
        // Checkout will be called to parse the current cart into an order.
        final CartDTO cart = loadCart(customerId);

        if (cart.getItems()
            .size() > 0) {
            final Order order = Order.builder()
                .items(cart.getItems()
                    .stream()
                    .map(item -> OrderItem.builder()
                        .product(productMapper.mapToEntity(item.getProduct())) //, Product.class))
                        .quantity(item.getQuantity())
                        .build())
                    .toList())
                .build();
        } else {
            throw new RuntimeException("Cannot checkout an empty cart!");
        }
    }
}
