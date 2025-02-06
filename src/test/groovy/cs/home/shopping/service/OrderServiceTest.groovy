import cs.home.shopping.dto.OrderDTO
import cs.home.shopping.model.entity.Order
import cs.home.shopping.model.repository.OrderRepository
import cs.home.shopping.service.OrderService
import org.modelmapper.ModelMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification
import spock.lang.Subject

@SpringBootTest
class OrderServiceTest extends Specification {

    @Autowired
    OrderRepository orderRepository

    @Autowired
    ModelMapper mapper

    @Subject
    OrderService orderService

    def setup() {
        orderService = new OrderService(orderRepository, mapper)
    }

    def "test findAll"() {
        given:
        List<Order> orders = [
                new Order(id: 1, customerId: 1, orderDate: new Date()),
                new Order(id: 2, customerId: 2, orderDate: new Date())
        ]
        orderRepository.findAll() >> orders

        when:
        List<OrderDTO> result = orderService.findAll()

        then:
        result.size() == 2
        result[0].id == 1
        result[1].id == 2
    }

    def "test findByCustomerId"() {
        given:
        Long customerId = 1L
        List<Order> orders = [
                new Order(id: 1, customerId: customerId, orderDate: new Date())
        ]
        orderRepository.findByCustomerId(customerId) >> orders

        when:
        List<OrderDTO> result = orderService.findByCustomerId(customerId)

        then:
        result.size() == 1
        result[0].id == 1
        result[0].customerId == customerId
    }
}