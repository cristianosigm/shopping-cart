package cs.home.shopping.model.entity;

import cs.home.shopping.model.definition.OrderStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "purchase_order", schema = "shopping_cart")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "customerId", referencedColumnName = "id")
    private Customer customer;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "orderId")
    private List<OrderItem> items;

    private BigDecimal totalPrice;

    private BigDecimal totalDiscount;

    private BigDecimal finalPrice;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;
}
