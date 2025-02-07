package cs.home.shopping.service

import cs.home.shopping.dto.CustomerDTO
import cs.home.shopping.exception.ItemNotFoundException
import cs.home.shopping.model.repository.CustomerRepository
import cs.home.shopping.shared.BaseTest
import org.modelmapper.ModelMapper

class CustomerServiceTest extends BaseTest {

    final repository = Mock(CustomerRepository)
    final mapper = new ModelMapper()
    final service = new CustomerService(repository, mapper)

    def "should save a valid customer"() {
        given:
        repository.save(_) >> customerRegular

        when:
        def result = service.save(mapper.map(customerRegular, CustomerDTO))

        then:
        resultMatchesExpected(result, mapper.map(customerRegular, CustomerDTO))

    }

    def "should load all customers"() {
        given:
        repository.findAll() >> Arrays.asList(customerVIP, customerRegular)

        when:
        final result = service.findAll()

        then:
        result.size() == 2
        resultMatchesExpected(result.get(0), mapper.map(customerVIP, CustomerDTO))
        resultMatchesExpected(result.get(1), mapper.map(customerRegular, CustomerDTO))
    }

    def "should load a customer's profile"() {
        given:
        repository.findById(_ as Long) >> Optional.of(customerVIP)

        when:
        final result = service.profile(2)

        then:
        resultMatchesExpected(result, mapper.map(customerVIP, CustomerDTO))
    }

    def "when loading an invalid customer then throw a valid exception"() {
        given:
        repository.findById(_ as Long) >> Optional.empty()

        when:
        service.profile(3)

        then:
        thrown(ItemNotFoundException)
    }

    private boolean resultMatchesExpected(CustomerDTO result, CustomerDTO expectedResult) {
        result.getId() == expectedResult.getId()
        result.getName() == expectedResult.getName()
        result.getIsVIP() == expectedResult.getIsVIP()
    }
}
