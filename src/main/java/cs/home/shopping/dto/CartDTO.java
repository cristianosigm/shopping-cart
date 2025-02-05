package cs.home.shopping.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartDTO {

    private Long id;

    private String sessionId;

    private Long customerId;

    @Builder.Default
    private List<CartItemDTO> items = new ArrayList<>();

    @Builder.Default
    private BigDecimal totalPrice = BigDecimal.ZERO;

    @Builder.Default
    private BigDecimal totalDiscount = BigDecimal.ZERO;

    @Builder.Default
    private BigDecimal finalPrice = BigDecimal.ZERO;

}
