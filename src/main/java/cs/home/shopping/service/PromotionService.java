package cs.home.shopping.service;

import cs.home.shopping.dto.PromotionDTO;
import cs.home.shopping.model.entity.CartItem;
import cs.home.shopping.model.entity.Promotion;
import cs.home.shopping.model.repository.PromotionRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PromotionService {

    private final PromotionRepository promotionRepository;
    private final ModelMapper mapper;

    @Autowired
    public PromotionService(PromotionRepository promotionRepository, ModelMapper mapper) {
        this.promotionRepository = promotionRepository;
        this.mapper = mapper;
    }

    public PromotionDTO save(PromotionDTO promotionDTO) {
        return mapper.map(promotionRepository.save(mapper.map(promotionDTO, Promotion.class)), PromotionDTO.class);
    }

    public List<PromotionDTO> findAll() {
        return mapToDTO(promotionRepository.findAll());
    }

    public List<Promotion> findAllActiveForVipStatus(Boolean customerIsVIP) {
        return promotionRepository.findAllByActiveTrueAndRequiresVIP(customerIsVIP);
    }

    public BigDecimal calculateDiscountBasedOnItems(List<CartItem> cartItems, List<Promotion> applicablePromotions) {
        // Filtering active promotion for items (I'm simply getting the first one for this challenge)
        final Promotion itemPromotion = applicablePromotions.stream()
            .filter(pr -> pr.getMinimumQuantity() > 0)
            .findFirst()
            .orElse(null);

        // Counting the number of items
        final Integer totalItems = cartItems.stream()
            .map(CartItem::getQuantity)
            .reduce(0, Integer::sum);

        // If the number of items reach the minimum required, discount the cheapest item
        if (itemPromotion != null && totalItems >= itemPromotion.getMinimumQuantity()) {
            // Finding the cheapest product
            return cartItems.stream()
                .map(item -> item.getProduct()
                    .getPrice())
                .min(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);
        }

        // Minimum item requirement not met.
        return BigDecimal.ZERO;
    }

    public BigDecimal calculateDiscountForVIP(Boolean customerIsVIP, BigDecimal basePrice,
                                              List<Promotion> applicablePromotions) {
        if (customerIsVIP) {
            // Getting the applicable promotion. IF there is more than one, it will just get the first for this challenge.
            final Promotion vipPromotion = applicablePromotions.stream()
                .filter(Promotion::getRequiresVIP)
                .findFirst()
                .orElse(null);

            if (vipPromotion != null && vipPromotion.getDiscountPercent()
                .compareTo(BigDecimal.ZERO) > 0) {
                // Deciding which discount would be better
                return basePrice.multiply(vipPromotion.getDiscountPercent()
                    .divide(BigDecimal.valueOf(100)));
            }
        }
        // Customer is not applicable
        return BigDecimal.ZERO;
    }

    private List<PromotionDTO> mapToDTO(List<Promotion> items) {
        return items.stream()
            .map(it -> mapper.map(it, PromotionDTO.class))
            .toList();
    }
}
