package cs.home.shopping.model.mapper;

import cs.home.shopping.dto.CartItemDTO;
import cs.home.shopping.model.entity.CartItem;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CartItemMapper {

    private final ModelMapper mapper;

    @Autowired
    public CartItemMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    public List<CartItemDTO> mapToDTO(List<CartItem> items) {
        return items.stream()
            .map(this::mapToDTO)
            .toList();
    }

    public CartItemDTO mapToDTO(CartItem item) {
        return this.mapper.map(item, CartItemDTO.class);
    }

    public List<CartItem> mapToEntity(List<CartItemDTO> items) {
        return items.stream()
            .map(this::mapToEntity)
            .toList();
    }

    public CartItem mapToEntity(CartItemDTO item) {
        return this.mapper.map(item, CartItem.class);
    }
}
