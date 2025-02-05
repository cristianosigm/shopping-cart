package cs.home.shopping.service;

import cs.home.shopping.dto.PromotionDTO;
import cs.home.shopping.model.entity.CartItem;
import cs.home.shopping.model.entity.Promotion;
import cs.home.shopping.model.mapper.PromotionMapper;
import cs.home.shopping.model.repository.PromotionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PromotionService {

    private final PromotionRepository promotionRepository;
    private final PromotionMapper promotionMapper;

    @Autowired
    public PromotionService(PromotionRepository promotionRepository, PromotionMapper promotionMapper) {
        this.promotionRepository = promotionRepository;
        this.promotionMapper = promotionMapper;
    }

    public PromotionDTO save(PromotionDTO promotionDTO) {
        return promotionMapper.mapToDTO(promotionRepository.save(promotionMapper.mapToEntity(promotionDTO)));
    }

    public List<PromotionDTO> findAll() {
        return promotionMapper.mapToDTO(promotionRepository.findAll());
    }

    public BigDecimal calculateDiscountBasedOnItems(List<CartItem> cartItems, List<Promotion> applicablePromotions) {
        // Filtering active promotion for items (I'm simply getting the first one for this challenge)
        final Promotion itemPromotion = applicablePromotions.parallelStream().filter(pr -> pr.getMinimumQuantity() > 0).findFirst().orElse(null);

        // Counting the number of items
        final Integer totalItems = cartItems.stream().map(CartItem::getQuantity).reduce(0, Integer::sum);

        // If the number of items reach the minimum required, discount the cheapest item
        if (totalItems >= itemPromotion.getMinimumQuantity()) {
            // Finding the cheapest product
            return cartItems.stream().map(item -> item.getProduct().getPrice()).min(BigDecimal::compareTo).orElse(BigDecimal.ZERO);
        }

        // Minimum item requirement not met.
        return BigDecimal.ZERO;
    }

    public BigDecimal calculateDiscountForVIP(Boolean customerIsVIP, BigDecimal basePrice, List<Promotion> applicablePromotions) {
        if (customerIsVIP) {
            final Promotion vipPromotion = applicablePromotions.stream().filter(Promotion::getRequiresVIP).findFirst().orElse(null);

            if (vipPromotion != null && vipPromotion.getDiscountPercent().compareTo(BigDecimal.ZERO) > 0) {
                // Deciding which discount would be better
                return basePrice.multiply(vipPromotion.getDiscountPercent().divide(BigDecimal.valueOf(100)));
            }
        }
        // Customer is not applicable
        return BigDecimal.ZERO;
    }

}
