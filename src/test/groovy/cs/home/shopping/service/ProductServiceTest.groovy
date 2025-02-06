import cs.home.shopping.dto.ProductDTO
import cs.home.shopping.exception.ItemNotFoundException
import cs.home.shopping.model.repository.ProductRepository
import cs.home.shopping.service.ProductService
import cs.home.shopping.shared.BaseTest
import org.modelmapper.ModelMapper

class ProductServiceTest extends BaseTest {

    final productRepository = Mock(ProductRepository)
    final mapper = new ModelMapper()
    final productService = new ProductService(productRepository, mapper)

    final products = Arrays.asList(shirt, jeans, dress)

    def "when querying all products then return all"() {
        given:
        productRepository.findAll() >> products

        when:
        final result = productService.findAll()

        then:
        resultMatchesExpected(result.get(0), mapper.map(products.get(0), ProductDTO))
        resultMatchesExpected(result.get(1), mapper.map(products.get(1), ProductDTO))
        resultMatchesExpected(result.get(2), mapper.map(products.get(2), ProductDTO))
    }

    def "when saving a valid set of products then success"() {
        given:
        final request = products
                .stream()
                .map(it -> mapper.map(it, ProductDTO))
                .toList()
        productRepository.saveAll(_ as List<ProductDTO>) >> products

        when:
        final result = productService.addAll(request)

        then:
        resultMatchesExpected(result.get(0), request.get(0))
        resultMatchesExpected(result.get(1), request.get(1))
        resultMatchesExpected(result.get(2), request.get(2))
    }

    def "when querying an existing product by id then return the product"() {
        given:
        productRepository.findById(_ as Long) >> Optional.of(jeans)

        when:
        final result = productService.findById(1)

        then:
        resultMatchesExpected(result, mapper.map(jeans, ProductDTO))
    }

    def "when fails to find a product by id then throw ItemNotFoundException"() {
        given:
        productRepository.findById(_ as Long) >> Optional.empty()

        when:
        productService.findById(1)

        then:
        thrown(ItemNotFoundException)
    }

    private boolean resultMatchesExpected(ProductDTO result, ProductDTO expectedResult) {
        result.getId() == expectedResult.getId()
        result.getName() == expectedResult.getName()
        result.getDescription() == expectedResult.getDescription()
        result.getPrice() == expectedResult.getPrice()
    }
}