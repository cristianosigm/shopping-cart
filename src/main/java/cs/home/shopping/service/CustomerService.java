package cs.home.shopping.service;

import cs.home.shopping.dto.CustomerDTO;
import cs.home.shopping.exception.ItemNotFoundException;
import cs.home.shopping.model.entity.Customer;
import cs.home.shopping.model.repository.CustomerRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final ModelMapper mapper;

    @Autowired
    public CustomerService(CustomerRepository customerRepository, ModelMapper mapper) {
        this.customerRepository = customerRepository;
        this.mapper = mapper;
    }

    public CustomerDTO save(CustomerDTO item) {
        return mapper.map(customerRepository.save(mapper.map(item, Customer.class)), CustomerDTO.class);
    }

    public List<CustomerDTO> findAll() {
        return mapper.map(customerRepository.findAll(), new TypeToken<List<CustomerDTO>>() {
        }.getType());
    }

    public CustomerDTO profile(Long customerId) {
        return mapper.map(customerRepository.findById(customerId)
            .orElseThrow(() -> new ItemNotFoundException("Profile not found.")), CustomerDTO.class);
    }
}
