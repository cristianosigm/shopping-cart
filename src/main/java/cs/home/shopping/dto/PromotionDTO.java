package cs.home.shopping.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PromotionDTO {

    private Long id;

    @NotEmpty
    private String name;

    private String description;

    @NotNull
    @Builder.Default
    private Integer minimumQuantity = 0;

    @Builder.Default
    private Boolean requiresVIP = Boolean.FALSE;

    @NotNull
    @Builder.Default
    private BigDecimal discountPercent = BigDecimal.ZERO;

    @NotNull
    @Builder.Default
    private Boolean active = Boolean.TRUE;
}
