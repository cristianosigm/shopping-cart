package cs.home.shopping.model.mapper;

import cs.home.shopping.dto.OrderDTO;
import cs.home.shopping.model.entity.Order;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderMapper {

    private final ModelMapper mapper;

    @Autowired
    public OrderMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    public List<OrderDTO> mapToDTO(List<Order> items) {
        return items.stream().map(this::mapToDTO).toList();
    }

    public OrderDTO mapToDTO(Order item) {
        return this.mapper.map(item, OrderDTO.class);
    }

    public Order mapToEntity(OrderDTO item) {
        return this.mapper.map(item, Order.class);
    }
}
