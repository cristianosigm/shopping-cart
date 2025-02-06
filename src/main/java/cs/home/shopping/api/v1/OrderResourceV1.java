package cs.home.shopping.api.v1;

import cs.home.shopping.api.v1.common.ReadResourceV1;
import cs.home.shopping.dto.OrderDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("/api/v1/orders")
public interface OrderResourceV1 extends ReadResourceV1 {

    @GetMapping("/by-customer")
    ResponseEntity<List<OrderDTO>> findByCustomerId(@RequestHeader Long customerId);
}
