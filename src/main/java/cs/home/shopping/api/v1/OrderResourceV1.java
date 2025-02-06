package cs.home.shopping.api.v1;

import cs.home.shopping.api.v1.common.DeleteResourceV1;
import cs.home.shopping.api.v1.common.ReadResourceV1;
import cs.home.shopping.dto.OrderDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("/api/v1/orders")
public interface OrderResourceV1 extends ReadResourceV1, DeleteResourceV1 {

    @PostMapping
    ResponseEntity<OrderDTO> save(@RequestBody @Valid OrderDTO item);

    @GetMapping("/by-customer/{id}")
    ResponseEntity<List<OrderDTO>> findByCustomerId(Long customerId);
}
