package cs.home.shopping.service

import cs.home.shopping.dto.CartDTO
import cs.home.shopping.model.entity.Cart
import cs.home.shopping.model.mapper.PromotionMapper
import cs.home.shopping.model.repository.CartRepository
import cs.home.shopping.model.repository.ProductRepository
import cs.home.shopping.model.repository.PromotionRepository
import cs.home.shopping.shared.BaseTest
import org.modelmapper.ModelMapper

class CartServiceTest extends BaseTest {

//    def cartMapper = new CartMapper(new ModelMapper())
    def promotionMapper = new PromotionMapper(new ModelMapper())

    def cartRepository = Mock(CartRepository)
    def promotionRepository = Mock(PromotionRepository)
    def productRepository = Mock(ProductRepository)

    def promotionService = new PromotionService(promotionRepository, promotionMapper)
    def cartService = new CartService(cartRepository, promotionService, promotionRepository, productRepository)

    def promotions = generatePromotions()

    // Challenge tests
    def "(ALL) when a #customerType Customer adds #items to cart then total should be #expected"() {
        given:
        cartRepository.findByCustomerId(_) >> Optional.of(Cart.builder()
                .id(1)
                .customerId(1)
                .customerIsVIP(isVIP)
                .items(generateItems(nrOfDresses, nrOfJeans, nrOfShirts))
                .build())

        promotionRepository.findAllByActiveTrueAndRequiresVIP(_) >> promotions

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
    def "when adding  "() {
        given:
        // declare the amount of products here

        expect:
        1 == 1

        where:
        productId | qty | result
        1         | 1   | true
        2         | 3   | true
    }

    def "when Products has #qty items and Customer is not VIP then Discount should be #discount"() {
        expect:
        qty > 0
        !customerRegular.isVIP
        discount < 101

        where:
        qty | discount
        1   | 0
        2   | 0
        3   | 100
    }

    def "when adding a product that was already added then increase quantity"() {
        expect:
        1 == 1
    }


}
