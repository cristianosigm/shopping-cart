package cs.home.shopping.model.entity;

import cs.home.shopping.model.definition.OrderStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "order", schema = "shopping")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    private Long customerId;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;
}
