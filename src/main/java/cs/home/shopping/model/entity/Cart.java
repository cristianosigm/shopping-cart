package cs.home.shopping.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "cart", schema = "shopping")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sessionId;

    private Long customerId;

    @NotNull
    @Builder.Default
    private Boolean customerIsVIP = Boolean.FALSE;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "cartId")
    @Builder.Default
    private List<CartItem> items = new ArrayList<>();

}
