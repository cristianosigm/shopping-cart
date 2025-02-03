package cs.home.shopping.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "promotion", schema = "shopping")
public class Promotion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    private String name;

    private String description;

    private Integer minimumQuantity;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "promotion_products", joinColumns = @JoinColumn(name = "promotionId"), inverseJoinColumns = @JoinColumn(name = "productId"))
    private List<Product> eligibleProducts;

    @Builder.Default
    private Boolean requiresVIP = Boolean.FALSE;

    @NotNull
    private Integer discountPercent;

    @Builder.Default
    private Boolean active = Boolean.TRUE;
}
