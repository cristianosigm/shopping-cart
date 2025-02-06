package cs.home.shopping.controller.v1;

import cs.home.shopping.api.v1.OrderResourceV1;
import cs.home.shopping.dto.OrderDTO;
import cs.home.shopping.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class OrderControllerV1 implements OrderResourceV1 {

    private final OrderService orderService;

    @Autowired
    public OrderControllerV1(OrderService orderService) {
        this.orderService = orderService;
    }

    @Override
    public ResponseEntity<List<OrderDTO>> findByCustomerId(Long customerId) {
        return ResponseEntity.ok(this.orderService.findByCustomerId(customerId));
    }

    @Override
    public ResponseEntity<?> findAll() {
        return ResponseEntity.ok(this.orderService.findAll());
    }
}
