package cs.home.shopping.model.mapper;

import cs.home.shopping.dto.CartDTO;
import cs.home.shopping.model.entity.Cart;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CartMapper {

    private final ModelMapper mapper;

    @Autowired
    public CartMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    public CartDTO mapToDTO(Cart item) {
        return this.mapper.map(item, CartDTO.class);
    }

}
