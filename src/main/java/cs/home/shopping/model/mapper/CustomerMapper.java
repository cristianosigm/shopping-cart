package cs.home.shopping.model.mapper;

import cs.home.shopping.dto.CustomerDTO;
import cs.home.shopping.model.entity.Customer;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CustomerMapper {

    private final ModelMapper mapper;

    @Autowired
    public CustomerMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    public List<CustomerDTO> mapToDTO(List<Customer> items) {
        return items.stream().map(this::mapToDTO).toList();
    }

    public CustomerDTO mapToDTO(Customer item) {
        return this.mapper.map(item, CustomerDTO.class);
    }

    public Customer mapToEntity(CustomerDTO item) {
        return this.mapper.map(item, Customer.class);
    }
}
