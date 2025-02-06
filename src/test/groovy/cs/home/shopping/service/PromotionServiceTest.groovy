import cs.home.shopping.dto.PromotionDTO
import cs.home.shopping.model.entity.CartItem
import cs.home.shopping.model.entity.Product
import cs.home.shopping.model.entity.Promotion
import cs.home.shopping.model.repository.PromotionRepository
import cs.home.shopping.service.PromotionService
import org.modelmapper.ModelMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification
import spock.lang.Subject

@SpringBootTest
class PromotionServiceTest extends Specification {

    @Autowired
    PromotionRepository promotionRepository

    @Autowired
    ModelMapper mapper

    @Subject
    PromotionService promotionService

    def setup() {
        promotionService = new PromotionService(promotionRepository, mapper)
    }

    def "test save"() {
        given:
        PromotionDTO promotionDTO = new PromotionDTO(id: 1, name: "Promo 1", discountPercent: BigDecimal.valueOf(10))
        Promotion promotion = new Promotion(id: 1, name: "Promo 1", discountPercent: BigDecimal.valueOf(10))
        promotionRepository.save(_) >> promotion

        when:
        PromotionDTO result = promotionService.save(promotionDTO)

        then:
        result.id == 1
        result.name == "Promo 1"
        result.discountPercent == BigDecimal.valueOf(10)
    }

    def "test findAll"() {
        given:
        List<Promotion> promotions = [
                new Promotion(id: 1, name: "Promo 1", discountPercent: BigDecimal.valueOf(10)),
                new Promotion(id: 2, name: "Promo 2", discountPercent: BigDecimal.valueOf(20))
        ]
        promotionRepository.findAll() >> promotions

        when:
        List<PromotionDTO> result = promotionService.findAll()

        then:
        result.size() == 2
        result[0].id == 1
        result[0].name == "Promo 1"
        result[0].discountPercent == BigDecimal.valueOf(10)
        result[1].id == 2
        result[1].name == "Promo 2"
        result[1].discountPercent == BigDecimal.valueOf(20)
    }

    def "test findAllActiveForVipStatus"() {
        given:
        Boolean customerIsVIP = true
        List<Promotion> promotions = [
                new Promotion(id: 1, name: "Promo 1", active: true, requiresVIP: true),
                new Promotion(id: 2, name: "Promo 2", active: true, requiresVIP: true)
        ]
        promotionRepository.findAllByActiveTrueAndRequiresVIP(customerIsVIP) >> promotions

        when:
        List<Promotion> result = promotionService.findAllActiveForVipStatus(customerIsVIP)

        then:
        result.size() == 2
        result[0].id == 1
        result[0].name == "Promo 1"
        result[1].id == 2
        result[1].name == "Promo 2"
    }

    def "test calculateDiscountBasedOnItems"() {
        given:
        List<CartItem> cartItems = [
                new CartItem(product: new Product(price: BigDecimal.valueOf(10)), quantity: 2),
                new CartItem(product: new Product(price: BigDecimal.valueOf(20)), quantity: 1)
        ]
        List<Promotion> promotions = [
                new Promotion(id: 1, name: "Promo 1", minimumQuantity: 2)
        ]

        when:
        BigDecimal result = promotionService.calculateDiscountBasedOnItems(cartItems, promotions)

        then:
        result == BigDecimal.valueOf(10)
    }

    def "test calculateDiscountForVIP"() {
        given:
        Boolean customerIsVIP = true
        BigDecimal basePrice = BigDecimal.valueOf(100)
        List<Promotion> promotions = [
                new Promotion(id: 1, name: "Promo 1", requiresVIP: true, discountPercent: BigDecimal.valueOf(10))
        ]

        when:
        BigDecimal result = promotionService.calculateDiscountForVIP(customerIsVIP, basePrice, promotions)

        then:
        result == BigDecimal.valueOf(10)
    }

    def "test calculateDiscountForVIP when not VIP"() {
        given:
        Boolean customerIsVIP = false
        BigDecimal basePrice = BigDecimal.valueOf(100)
        List<Promotion> promotions = [
                new Promotion(id: 1, name: "Promo 1", requiresVIP: true, discountPercent: BigDecimal.valueOf(10))
        ]

        when:
        BigDecimal result = promotionService.calculateDiscountForVIP(customerIsVIP, basePrice, promotions)

        then:
        result == BigDecimal.ZERO
    }
}