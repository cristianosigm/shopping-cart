package cs.home.shopping.dto.mapper;

import cs.home.shopping.dto.ProductCreateDTO;
import cs.home.shopping.model.entity.Product;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductMapper {

    public Product map(ProductCreateDTO from) {
        return Product.builder()
                .id(from.getId())
                .name(from.getName())
                .price(from.getPrice())
                .build();
    }

    public List<Product> map(List<ProductCreateDTO> from) {
        return from.stream().map(this::map).toList();
    }

}
