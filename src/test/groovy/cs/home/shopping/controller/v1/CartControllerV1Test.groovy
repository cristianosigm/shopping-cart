import cs.home.shopping.controller.v1.CartControllerV1
import cs.home.shopping.dto.CartDTO
import cs.home.shopping.service.CartService
import cs.home.shopping.shared.BaseTest
import org.modelmapper.ModelMapper
import org.springframework.http.ResponseEntity

class CartControllerV1Test extends BaseTest {

    final mapper = new ModelMapper()
    final cartService = Mock(CartService)
    final cartControllerV1 = new CartControllerV1(cartService)

    def "when addProduct is hit then return a valid result"() {
        when:
        final result = cartControllerV1.addProduct(1, 1, 1)

        then:
        1 * cartService.addProduct(1, 1, 1)
        result.statusCode.value() == 200
        result.body.message == "Product added."
    }

    def "when removeProduct is hit then return a valid result"() {
        when:
        final result = cartControllerV1.removeProduct(1, 2)

        then:
        1 * cartService.removeProduct(1, 2)
        result.statusCode.value() == 200
        result.body.message == "Product removed."
    }

    def "when clearCart is hit then return a valid result"() {
        when:
        final result = cartControllerV1.clearCart(1)

        then:
        1 * cartService.clearCart(1)
        result.statusCode.value() == 200
        result.body.message == "Cart successfully cleared."
    }

    def "when loadCart is hit then return a valid cart"() {
        given:
        cartService.loadCart(_ as Long) >> mapper.map(cartVIP, CartDTO)

        when:
        final ResponseEntity<CartDTO> result = cartControllerV1.loadCart(1)

        then:
        1 * cartService.loadCart(1)
        result.statusCode.value() == 200
    }

    def "when checkout is hit then return a valid result"() {
        when:
        final result = cartControllerV1.checkout(4)

        then:
        1 * cartService.checkout(4)
        result.statusCode.value() == 200
        result.body.message == "Checkout operation successful."
    }
}