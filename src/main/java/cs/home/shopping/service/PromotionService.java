package cs.home.shopping.service;

import cs.home.shopping.dto.PromotionDTO;
import cs.home.shopping.model.mapper.PromotionMapper;
import cs.home.shopping.model.repository.PromotionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PromotionService {

    private final PromotionRepository promotionRepository;
    private final PromotionMapper promotionMapper;

    @Autowired
    public PromotionService(PromotionRepository promotionRepository, PromotionMapper promotionMapper) {
        this.promotionRepository = promotionRepository;
        this.promotionMapper = promotionMapper;
    }

    public PromotionDTO save(PromotionDTO promotionDTO) {
        return promotionMapper.mapToDTO(promotionRepository.save(promotionMapper.mapToEntity(promotionDTO)));
    }

    public List<PromotionDTO> findAll() {
        return promotionMapper.mapToDTO(promotionRepository.findAll());
    }

}
