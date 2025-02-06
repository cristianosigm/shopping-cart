import cs.home.shopping.controller.v1.CustomerControllerV1
import cs.home.shopping.dto.CustomerDTO
import cs.home.shopping.service.CustomerService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.ResponseEntity
import spock.lang.Specification
import spock.lang.Subject


@SpringBootTest
class CustomerControllerV1Test extends Specification {

    @Autowired
    CustomerService customerService

    @Subject
    CustomerControllerV1 customerControllerV1

    def setup() {
        customerControllerV1 = new CustomerControllerV1(customerService)
    }

    def "test findAll"() {
        given:
        List<CustomerDTO> customers = [
                new CustomerDTO(id: 1, name: "Customer 1"),
                new CustomerDTO(id: 2, name: "Customer 2")
        ]
        customerService.findAll() >> customers

        when:
        ResponseEntity<List<CustomerDTO>> response = customerControllerV1.findAll()

        then:
        1 * customerService.findAll()
        response.statusCode.value() == 200
        response.body.size() == 2
        response.body[0].id == 1
        response.body[0].name == "Customer 1"
        response.body[1].id == 2
        response.body[1].name == "Customer 2"
    }

    def "test profile"() {
        given:
        Long customerId = 1L
        CustomerDTO customerDTO = new CustomerDTO(id: customerId, name: "Customer 1")
        customerService.profile(customerId) >> customerDTO

        when:
        ResponseEntity<CustomerDTO> response = customerControllerV1.profile(customerId)

        then:
        1 * customerService.profile(customerId)
        response.statusCode.value() == 200
        response.body.id == customerId
        response.body.name == "Customer 1"
    }

    def "test save"() {
        given:
        CustomerDTO customerDTO = new CustomerDTO(id: 1, name: "Customer 1")
        customerService.save(customerDTO) >> customerDTO

        when:
        ResponseEntity<CustomerDTO> response = customerControllerV1.save(customerDTO)

        then:
        1 * customerService.save(customerDTO)
        response.statusCode.value() == 200
        response.body.id == 1
        response.body.name == "Customer 1"
    }
}