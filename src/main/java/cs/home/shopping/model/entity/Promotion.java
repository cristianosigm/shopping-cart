package cs.home.shopping.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "promotion")
public class Promotion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    private String name;

    private String description;

    @NotNull
    @Builder.Default
    private Integer minimumQuantity = 0;

    @NotNull
    @Builder.Default
    private Integer freeItems = 0;

    @Builder.Default
    private Boolean requiresVIP = Boolean.FALSE;

    @NotNull
    @Builder.Default
    private BigDecimal discountPercent = BigDecimal.ZERO;

    @NotNull
    @Builder.Default
    private Boolean active = Boolean.TRUE;
}
