package cs.home.shopping.controller.v1;

import cs.home.shopping.api.v1.CustomerResourceV1;
import cs.home.shopping.dto.CustomerDTO;
import cs.home.shopping.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CustomerControllerV1 implements CustomerResourceV1 {

    private final CustomerService customerService;

    @Autowired
    public CustomerControllerV1(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Override
    public ResponseEntity<List<CustomerDTO>> findAll() {
        return ResponseEntity.ok(customerService.findAll());
    }

    @Override
    public ResponseEntity<CustomerDTO> profile(Long customerId) {
        return ResponseEntity.ok(customerService.profile(customerId));
    }

    @Override
    public ResponseEntity<CustomerDTO> save(CustomerDTO item) {
        return ResponseEntity.ok(customerService.save(item));
    }
}
