package cs.home.shopping.service;

import java.math.BigDecimal;
import java.util.List;

public class PromotionService {

    public BigDecimal getDiscountForProduct(Long productId) {
        return BigDecimal.ZERO;
    }

    public BigDecimal getDiscount(List<Long> productIds, Boolean isVIP) {
        return BigDecimal.ZERO;
    }

}
