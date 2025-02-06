package cs.home.shopping.model.mapper;

import cs.home.shopping.dto.OrderItemDTO;
import cs.home.shopping.model.entity.OrderItem;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderItemMapper {

    private final ModelMapper mapper;

    @Autowired
    public OrderItemMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    public List<OrderItemDTO> mapToDTO(List<OrderItem> items) {
        return items.stream()
            .map(this::mapToDTO)
            .toList();
    }

    public OrderItemDTO mapToDTO(OrderItem item) {
        return this.mapper.map(item, OrderItemDTO.class);
    }

    public List<OrderItem> mapToEntity(List<OrderItemDTO> items) {
        return items.stream()
            .map(this::mapToEntity)
            .toList();
    }

    public OrderItem mapToEntity(OrderItemDTO item) {
        return this.mapper.map(item, OrderItem.class);
    }
}
