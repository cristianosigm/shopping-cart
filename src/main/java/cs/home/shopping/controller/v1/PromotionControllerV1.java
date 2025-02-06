package cs.home.shopping.controller.v1;

import cs.home.shopping.api.v1.PromotionResourceV1;
import cs.home.shopping.dto.PromotionDTO;
import cs.home.shopping.service.PromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PromotionControllerV1 implements PromotionResourceV1 {

    private final PromotionService promotionService;

    @Autowired
    public PromotionControllerV1(PromotionService promotionService) {
        this.promotionService = promotionService;
    }

    @Override
    public ResponseEntity<List<PromotionDTO>> findAll() {
        return ResponseEntity.ok(promotionService.findAll());
    }

    @Override
    public ResponseEntity<PromotionDTO> save(PromotionDTO promotion) {
        return ResponseEntity.ok(promotionService.save(promotion));
    }
}
