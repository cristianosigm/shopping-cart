package cs.home.shopping.api.v1;

import cs.home.shopping.api.v1.common.DeleteResourceV1;
import cs.home.shopping.api.v1.common.ReadResourceV1;
import cs.home.shopping.dto.OrderDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/v1/orders")
public interface OrderResourceV1 extends ReadResourceV1, DeleteResourceV1 {

    @PostMapping
    ResponseEntity<?> save(@RequestBody @Valid OrderDTO item);

}
