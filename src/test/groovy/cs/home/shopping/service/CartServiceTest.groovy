package cs.home.shopping.service

import cs.home.shopping.dto.CartDTO
import cs.home.shopping.model.entity.Cart
import cs.home.shopping.model.entity.CartItem
import cs.home.shopping.model.repository.*
import cs.home.shopping.shared.BaseTest
import org.modelmapper.ModelMapper

class CartServiceTest extends BaseTest {

    def mapper = new ModelMapper()
    def cartRepository = Mock(CartRepository)
    def cartItemRepository = Mock(CartItemRepository)
    def promotionRepository = Mock(PromotionRepository)
    def productRepository = Mock(ProductRepository)
    def orderRepository = Mock(OrderRepository)

    def promotionService = new PromotionService(promotionRepository, mapper)
    def cartService = new CartService(cartRepository, cartItemRepository, promotionService, productRepository,
            orderRepository, mapper)

    def promotions = generatePromotions()

    // Challenge tests
    def "when a #customerType Customer adds #items to cart then total should be #expected"() {
        given:
        cartRepository.findByCustomerId(_ as Long) >> Optional.of(Cart.builder()
                .id(1)
                .customerId(1)
                .customerIsVIP(isVIP)
                .items(generateItems(nrOfDresses, nrOfJeans, nrOfShirts))
                .build())

        promotionRepository.findAllByActiveTrueAndRequiresVIP(_ as Boolean) >> promotions

        when:
        CartDTO response = cartService.loadCart(1)

        then:
        response.getFinalPrice() == BigDecimal.valueOf(expected)

        where:
        customerType | items                   | isVIP | nrOfDresses | nrOfJeans | nrOfShirts | expected
        "regular"    | "3 shirts"              | false | 0           | 0         | 3          | 71.98
        "regular"    | "2 shirts and 2 jeans"  | false | 0           | 2         | 2          | 166.99
        "VIP"        | "3 dresses"             | true  | 3           | 0         | 0          | 161.50
        "VIP"        | "2 jeans and 2 dresses" | true  | 2           | 2         | 0          | 227.00
        "VIP"        | "4 shirts and 1 jeans"  | true  | 0           | 1         | 4          | 173.47
    }

    // Additional tests
    def "when adding #details products then should #expected"() {
        given:
        def items = new ArrayList<CartItem>()
        items.add(CartItem.builder()
                .id(1)
                .product(shirt)
                .quantity(1)
                .build())

        cartRepository.findByCustomerId(_ as Long) >> Optional.of(Cart.builder()
                .id(1)
                .customerId(1)
                .items(items)
                .build())

        productRepository.findById(1) >> Optional.of(shirt)
        productRepository.findById(2) >> Optional.of(jeans)

        Cart captured
        cartRepository.save(_ as Cart) >> { arguments -> captured = arguments[0] }

        when:
        cartService.addProduct(1, productId, quantity)

        then:
        captured.items.size() == expectSize
        captured.items.get(0).getQuantity() == qtyShirts

        where:
        details    | expected          | productId | quantity | expectSize | qtyShirts
        "new"      | "create an item"  | 2         | 1        | 2          | 1
        "existing" | "add to existing" | 1         | 1        | 1          | 2
    }

}
