package cs.home.shopping.model.entity;

import cs.home.shopping.model.definition.OrderStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

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
    private Long id;

    private Long customerId;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "orderId")
    private List<OrderItem> items;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;
}
