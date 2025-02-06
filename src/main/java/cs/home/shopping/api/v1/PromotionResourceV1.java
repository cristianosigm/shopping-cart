package cs.home.shopping.api.v1;

import cs.home.shopping.dto.PromotionDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("/api/v1/promotions")
public interface PromotionResourceV1 {

    @GetMapping
    ResponseEntity<List<PromotionDTO>> findAll();

    @PostMapping
    ResponseEntity<PromotionDTO> save(@RequestBody @Valid PromotionDTO promotion);
}
