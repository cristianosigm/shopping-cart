package cs.home.shopping.service;

import cs.home.shopping.dto.CustomerDTO;
import cs.home.shopping.model.mapper.CustomerMapper;
import cs.home.shopping.model.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper mapper;

    @Autowired
    public CustomerService(CustomerRepository customerRepository, CustomerMapper mapper) {
        this.customerRepository = customerRepository;
        this.mapper = mapper;
    }

    public CustomerDTO save(CustomerDTO item) {
        return this.mapper.mapToDTO(this.customerRepository.save(this.mapper.mapToEntity(item)));
    }

    public List<CustomerDTO> findAll() {
        return this.mapper.mapToDTO(this.customerRepository.findAll());
    }

}
