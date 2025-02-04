package cs.home.shopping.service;

import cs.home.shopping.dto.OrderDTO;
import cs.home.shopping.model.mapper.OrderMapper;
import cs.home.shopping.model.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper mapper;

    @Autowired
    public OrderService(OrderRepository orderRepository, OrderMapper mapper) {
        this.orderRepository = orderRepository;
        this.mapper = mapper;
    }

    public List<OrderDTO> findAll() {
        return this.mapper.mapToDTO(this.orderRepository.findAll());
    }

    public List<OrderDTO> findByCustomerId(Long customerId) {
        return this.mapper.mapToDTO(this.orderRepository.findByCustomerId(customerId));
    }

    public OrderDTO save(OrderDTO item) {
        return this.mapper.mapToDTO(this.orderRepository.save(this.mapper.mapToEntity(item)));
    }

}
