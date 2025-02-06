import cs.home.shopping.controller.v1.CartControllerV1
import cs.home.shopping.dto.CartDTO
import cs.home.shopping.dto.response.SuccessResponseDTO
import cs.home.shopping.service.CartService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.ResponseEntity
import spock.lang.Specification
import spock.lang.Subject

@SpringBootTest
class CartControllerV1Test extends Specification {

    @Autowired
    CartService cartService

    @Subject
    CartControllerV1 cartControllerV1

    def setup() {
        cartControllerV1 = new CartControllerV1(cartService)
    }

    def "test addProduct"() {
        given:
        Long customerId = 1L
        Long productId = 1L
        Integer quantity = 2

        when:
        ResponseEntity<SuccessResponseDTO> response = cartControllerV1.addProduct(customerId, productId, quantity)

        then:
        1 * cartService.addProduct(customerId, productId, quantity)
        response.statusCode.value() == 200
        response.body.message == "Product added."
    }

    def "test removeProduct"() {
        given:
        Long customerId = 1L
        Long productId = 1L

        when:
        ResponseEntity<SuccessResponseDTO> response = cartControllerV1.removeProduct(customerId, productId)

        then:
        1 * cartService.removeProduct(customerId, productId)
        response.statusCode.value() == 200
        response.body.message == "Product removed."
    }

    def "test clearCart"() {
        given:
        Long customerId = 1L

        when:
        ResponseEntity<SuccessResponseDTO> response = cartControllerV1.clearCart(customerId)

        then:
        1 * cartService.clearCart(customerId)
        response.statusCode.value() == 200
        response.body.message == "Cart successfully cleared."
    }

    def "test loadCart"() {
        given:
        Long customerId = 1L
        CartDTO cartDTO = new CartDTO()
        cartService.loadCart(customerId) >> cartDTO

        when:
        ResponseEntity<CartDTO> response = cartControllerV1.loadCart(customerId)

        then:
        1 * cartService.loadCart(customerId)
        response.statusCode.value() == 200
        response.body == cartDTO
    }

    def "test checkout"() {
        given:
        Long customerId = 1L

        when:
        ResponseEntity<SuccessResponseDTO> response = cartControllerV1.checkout(customerId)

        then:
        1 * cartService.checkout(customerId)
        response.statusCode.value() == 200
        response.body.message == "Checkout operation successful."
    }
}