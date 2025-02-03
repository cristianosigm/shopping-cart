package cs.home.shopping.model.mapper;

import cs.home.shopping.dto.PromotionDTO;
import cs.home.shopping.model.entity.Promotion;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PromotionMapper {

    private final ModelMapper mapper;

    @Autowired
    public PromotionMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    public List<PromotionDTO> mapToDTO(List<Promotion> items) {
        return items.stream().map(this::mapToDTO).toList();
    }

    public PromotionDTO mapToDTO(Promotion item) {
        return this.mapper.map(item, PromotionDTO.class);
    }

    public List<Promotion> mapToEntity(List<PromotionDTO> items) {
        return items.stream().map(this::mapToEntity).toList();
    }

    public Promotion mapToEntity(PromotionDTO item) {
        return this.mapper.map(item, Promotion.class);
    }

}
