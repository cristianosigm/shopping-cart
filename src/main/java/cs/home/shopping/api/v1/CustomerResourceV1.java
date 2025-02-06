package cs.home.shopping.api.v1;

import cs.home.shopping.api.v1.common.ReadResourceV1;
import cs.home.shopping.dto.CustomerDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/customers")
public interface CustomerResourceV1 extends ReadResourceV1 {

    @GetMapping
    ResponseEntity<CustomerDTO> profile(@RequestHeader Long customerId);

    @PostMapping
    ResponseEntity<?> save(@RequestBody @Valid CustomerDTO item);
}
