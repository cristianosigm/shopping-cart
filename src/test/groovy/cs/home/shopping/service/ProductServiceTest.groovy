import cs.home.shopping.dto.ProductDTO
import cs.home.shopping.exception.ItemNotFoundException
import cs.home.shopping.model.entity.Product
import cs.home.shopping.model.repository.ProductRepository
import cs.home.shopping.service.ProductService
import org.modelmapper.ModelMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification
import spock.lang.Subject

@SpringBootTest
class ProductServiceTest extends Specification {

    @Autowired
    ProductRepository productRepository

    @Autowired
    ModelMapper mapper

    @Subject
    ProductService productService

    def setup() {
        productService = new ProductService(productRepository, mapper)
    }

    def "test findAll"() {
        given:
        List<Product> products = [
                new Product(id: 1, name: "Product 1", price: 10.0),
                new Product(id: 2, name: "Product 2", price: 20.0)
        ]
        productRepository.findAll() >> products

        when:
        List<ProductDTO> result = productService.findAll()

        then:
        result.size() == 2
        result[0].id == 1
        result[0].name == "Product 1"
        result[0].price == 10.0
        result[1].id == 2
        result[1].name == "Product 2"
        result[1].price == 20.0
    }

    def "test addAll"() {
        given:
        List<ProductDTO> productDTOs = [
                new ProductDTO(id: 1, name: "Product 1", price: 10.0),
                new ProductDTO(id: 2, name: "Product 2", price: 20.0)
        ]
        List<Product> products = [
                new Product(id: 1, name: "Product 1", price: 10.0),
                new Product(id: 2, name: "Product 2", price: 20.0)
        ]
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