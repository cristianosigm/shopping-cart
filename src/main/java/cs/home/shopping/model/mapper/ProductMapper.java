package cs.home.shopping.model.mapper;

import cs.home.shopping.dto.ProductDTO;
import cs.home.shopping.model.entity.Product;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductMapper {

    private final ModelMapper mapper;

    @Autowired
    public ProductMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    public List<ProductDTO> mapToDTO(List<Product> items) {
        return items.stream()
            .map(this::mapToDTO)
            .toList();
    }

    public ProductDTO mapToDTO(Product item) {
        return this.mapper.map(item, ProductDTO.class);
    }

    public List<Product> mapToEntity(List<ProductDTO> items) {
        return items.stream()
            .map(this::mapToEntity)
            .toList();
    }

    public Product mapToEntity(ProductDTO item) {
        return this.mapper.map(item, Product.class);
    }
}
