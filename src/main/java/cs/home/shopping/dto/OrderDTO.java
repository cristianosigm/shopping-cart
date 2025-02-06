package cs.home.shopping.dto;

import cs.home.shopping.model.definition.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDTO {

    private Long id;

    private List<OrderItemDTO> items;

    @Builder.Default
    private OrderStatus status = OrderStatus.PENDING_PAYMENT;
}
