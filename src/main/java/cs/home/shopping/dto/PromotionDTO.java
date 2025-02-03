package cs.home.shopping.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PromotionDTO {

    private Long id;

    private String name;

    private String description;

    private Integer minimumQuantity;

    private List<ProductDTO> eligibleProducts;
    
    @Builder.Default
    private Boolean requiresVIP = Boolean.FALSE;

    private Integer discountPercent;

    @Builder.Default
    private Boolean active = Boolean.TRUE;
}
