package cs.home.shopping.api.v1;

import cs.home.shopping.api.v1.common.ReadResourceV1;
import cs.home.shopping.dto.PromotionDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/v1/promotions")
public interface PromotionResourceV1 extends ReadResourceV1 {

    @PostMapping
    ResponseEntity<PromotionDTO> save(@RequestBody @Valid PromotionDTO promotion);
}
