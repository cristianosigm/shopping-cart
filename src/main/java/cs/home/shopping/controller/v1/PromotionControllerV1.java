package cs.home.shopping.controller.v1;

import cs.home.shopping.api.v1.PromotionResourceV1;
import cs.home.shopping.dto.PromotionParametersDTO;
import cs.home.shopping.model.entity.Promotion;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PromotionControllerV1 implements PromotionResourceV1 {

    @Override
    public ResponseEntity<List<Promotion>> getApplicablePromotions(PromotionParametersDTO parameters) {
        return ResponseEntity.ok(List.of());
    }

}
