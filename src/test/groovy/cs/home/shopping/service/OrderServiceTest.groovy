import cs.home.shopping.dto.OrderDTO
import cs.home.shopping.model.entity.Order
import cs.home.shopping.model.repository.OrderRepository
import cs.home.shopping.service.OrderService
import cs.home.shopping.shared.BaseTest
import org.modelmapper.ModelMapper

class OrderServiceTest extends BaseTest {

    final orderRepository = Mock(OrderRepository)
    final mapper = new ModelMapper()
    final orderService = new OrderService(orderRepository, mapper)

    def orderOne = Order.builder()
            .id(1)
            .customer(customerRegular)
            .items(orderItemsOne)
            .totalPrice(BigDecimal.valueOf(100))
            .totalDiscount(BigDecimal.ZERO)
            .finalPrice(BigDecimal.valueOf(100))
            .build()

    def orderTwo = Order.builder()
            .id(2)
            .customer(customerVIP)
            .items(orderItemsTwo)
            .totalPrice(BigDecimal.valueOf(200))
            .totalDiscount(BigDecimal.ZERO)
            .finalPrice(BigDecimal.valueOf(200))
            .build()


    def "when querying all orders then success"() {
        given:
        orderRepository.findAll() >> Arrays.asList(orderOne, orderTwo)

        when:
        List<OrderDTO> result = orderService.findAll()

        then:
        result.size() == 2
        result.get(0).getId() == 1
        result.get(0).getItems().size() == 1
        result.get(1).getId() == 2
        result.get(1).getItems().size() == 2
    }

    def "when querying orders by customer then return valid orders"() {
        given:
        orderRepository.findByCustomerId(_ as Long) >> Arrays.asList(orderOne)

        when:
        List<OrderDTO> result = orderService.findByCustomerId(1)

        then:
        result.size() == 1
        result.get(0).getId() == 1
        result.get(0).getItems().size() == 1
        result.get(0).getCustomer().getId() == 2
    }
}