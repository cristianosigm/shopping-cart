package cs.home.shopping.service

import cs.home.shopping.dto.CustomerDTO
import cs.home.shopping.model.entity.Customer
import cs.home.shopping.model.repository.CustomerRepository
import org.modelmapper.ModelMapper
import spock.lang.Specification

class CustomerServiceTest extends Specification {

    def repository = Mock(CustomerRepository)
    def mapper = new ModelMapper()
    def service = new CustomerService(repository, mapper)

    def validEntity = Customer.builder()
            .id(1)
            .name("Mocked Name")
            .isVIP(true)
            .build()
    def validEntityList = Arrays.asList(
            Customer.builder()
                    .id(1)
                    .name("First Mocked Name")
                    .isVIP(true)
                    .build(),
            Customer.builder()
                    .id(2)
                    .name("Second Mocked Name")
                    .isVIP(false)
                    .build(),
            Customer.builder()
                    .id(3)
                    .name("Third Mocked Name")
                    .isVIP(true)
                    .build()
    )

    def "should save a valid customer"() {
        given:
        def validCustomer = CustomerDTO.builder().build()
        repository.save(_) >> validEntity

        when:
        def response = service.save(validCustomer)

        then:
        response !== null
        response.id == validEntity.id
        response.name == validEntity.name
        response.isVIP == validEntity.isVIP
    }

    def "should load all customers"() {
        given:
        repository.findAll() >> validEntityList

        when:
        def response = service.findAll()

        then:
        response.size() == 3
        response.get(0).id == 1
        response.get(0).isVIP == true
        response.get(1).id == 2
        response.get(1).isVIP == false
    }

}
