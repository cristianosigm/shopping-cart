import cs.home.shopping.dto.ProductDTO
import cs.home.shopping.exception.ItemNotFoundException
import cs.home.shopping.model.entity.Product
import cs.home.shopping.model.repository.ProductRepository
import cs.home.shopping.service.ProductService
import cs.home.shopping.shared.BaseTest
import org.modelmapper.ModelMapper

class ProductServiceTest extends BaseTest {

    final productRepository = Mock(ProductRepository)
    final mapper = new ModelMapper()
    final productService = new ProductService(productRepository, mapper)

    def products = Arrays.asList(shirt, jeans, dress)

    def "test findAll"() {
        given:
        productRepository.findAll() >> products

        when:
        List<ProductDTO> result = productService.findAll()

        then:
        result.size() == 3
        result.get(0).getId() == 1
        result.get(0).getName() == "T-Shirt"
        result.get(0).getPrice() == 35.99
        result.get(0).getId() == 2
        result.get(0).getName() == "Jeans"
        result.get(0).getPrice() == 65.50
    }

    def "test addAll"() {
        given:
        def request = Arrays
        productRepository.saveAll(_) >> products

        when:
        List<ProductDTO> result = productService.addAll(productDTOs)

        then:
        result.size() == 2
        result[0].id == 1
        result[0].name == "Product 1"
        result[0].price == 10.0
        result[1].id == 2
        result[1].name == "Product 2"
        result[1].price == 20.0
    }

    def "test findById"() {
        given:
        Long productId = 1L
        Product product = new Product(id: productId, name: "Product 1", price: 10.0)
        productRepository.findById(productId) >> Optional.of(product)

        when:
        ProductDTO result = productService.findById(productId)

        then:
        result.id == productId
        result.name == "Product 1"
        result.price == 10.0
    }

    def "test findById throws ItemNotFoundException"() {
        given:
        Long productId = 1L
        productRepository.findById(productId) >> Optional.empty()

        when:
        productService.findById(productId)

        then:
        thrown(ItemNotFoundException)
    }
}