package cs.home.shopping.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartDTO {

    private Long id;

    private String sessionId;

    private Long customerId;

    private List<CartItemDTO> items;

    private List<PromotionDTO> applicablePromotions;

    private BigDecimal totalPrice;

    private BigDecimal totalDiscount;

}
