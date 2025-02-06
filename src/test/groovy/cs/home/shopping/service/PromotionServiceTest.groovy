import cs.home.shopping.dto.PromotionDTO
import cs.home.shopping.model.entity.Promotion
import cs.home.shopping.model.repository.PromotionRepository
import cs.home.shopping.service.PromotionService
import cs.home.shopping.shared.BaseTest
import org.modelmapper.ModelMapper

class PromotionServiceTest extends BaseTest {

    final promotionRepository = Mock(PromotionRepository)
    final mapper = new ModelMapper()
    final promotionService = new PromotionService(promotionRepository, mapper)

    def "when saving a valid promotion then success"() {
        given:
        final expectedResult = mapper.map(promotionVIP, PromotionDTO.class)
        promotionRepository.save(_ as Promotion) >> promotionVIP

        when:
        final result = promotionService.save(PromotionDTO.builder().id(1).build())

        then:
        resultMatchesExpected(result, expectedResult)
    }

    def "when querying all promotions then return all"() {
        given:
        final expectedItem1 = mapper.map(promotionVIP, PromotionDTO)
        final expectedItem2 = mapper.map(promotionItems, PromotionDTO)
        final expectedItem3 = mapper.map(promotionInactive, PromotionDTO)

        promotionRepository.findAll() >> Arrays.asList(promotionVIP, promotionItems, promotionInactive)

        when:
        def result = promotionService.findAll()

        then:
        result.size() == 3
        resultMatchesExpected(expectedItem1, result.get(0))
        resultMatchesExpected(expectedItem2, result.get(1))
        resultMatchesExpected(expectedItem3, result.get(2))
    }

    def "when querying active promotions for VIPs then return applicable only"() {
        given:
        promotionRepository.findAllByActiveTrueAndRequiresVIP(_ as Boolean) >> Arrays.asList(promotionVIP)

        when:
        final result = promotionService.findAllActiveForVipStatus(true)

        then:
        result.size() == 1
        resultMatchesExpected(promotionVIP, result.get(0))
    }

    def "when calculating discount on #items for items-based promotion and regular customer then return #expected"() {
        given:
        final cartItems = generateItems(nrOfDresses, nrOfJeans, nrOfShirts)
        final promotions = Arrays.asList(promotionVIP, promotionItems)

        when:
        final result = promotionService.calculateDiscountBasedOnItems(cartItems, promotions)

        then:
        result == BigDecimal.valueOf(expected)

        where:
        items                   | nrOfDresses | nrOfJeans | nrOfShirts | expected
        "2 dresses"             | 2           | 0         | 0          | 0.00
        "3 shirts"              | 0           | 0         | 3          | 35.99
        "2 shirts and 2 jeans"  | 0           | 2         | 2          | 35.99
        "2 dresses and 2 jeans" | 2           | 2         | 0          | 65.50
    }

    def "if customer is #userType and spent #totalPrice, the discount for a VIP-based promotion is #discount"() {
        given:
        final promotions = Arrays.asList(promotionVIP, promotionItems)

        when:
        final result = promotionService.calculateDiscountForVIP(isVIP, totalPrice, promotions)

        then:
        result == BigDecimal.valueOf(discount)

        where:
        userType  | totalPrice | isVIP | discount
        "VIP"     | 100.00     | true  | 15.00
        "VIP"     | 1000.00    | true  | 150.00
        "regular" | 850.00     | false | 0.00

    }

    private boolean resultMatchesExpected(Promotion result, Promotion expected) {
        return resultMatchesExpected(
                mapper.map(result, PromotionDTO),
                mapper.map(expected, PromotionDTO),
        )
    }

    private boolean resultMatchesExpected(PromotionDTO result, PromotionDTO expectedResult) {
        result.getId() == expectedResult.getId()
        result.getName() == expectedResult.getName()
        result.getDescription() == expectedResult.getDescription()
        result.getMinimumQuantity() == expectedResult.getMinimumQuantity()
        result.getRequiresVIP() == expectedResult.getRequiresVIP()
        result.getDiscountPercent() == expectedResult.getDiscountPercent()
        result.getActive() == expectedResult.getActive()
    }
}