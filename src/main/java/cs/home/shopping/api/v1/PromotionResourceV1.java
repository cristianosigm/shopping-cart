package cs.home.shopping.api.v1;

import cs.home.shopping.dto.PromotionParametersDTO;
import cs.home.shopping.model.entity.Promotion;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("/api/v1/promotions")
public interface PromotionResourceV1 {

    @GetMapping("/applicable")
    ResponseEntity<List<Promotion>> getApplicablePromotions(@RequestBody @Valid PromotionParametersDTO parameters);
}
