package cs.home.shopping.service;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PromotionService {

    public BigDecimal getDiscountForProduct(Long productId) {
        return BigDecimal.ZERO;
    }

    public BigDecimal getDiscount(List<Long> productIds, Boolean isVIP) {
        return BigDecimal.ZERO;
    }

}
