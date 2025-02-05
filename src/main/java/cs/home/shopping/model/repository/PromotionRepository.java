package cs.home.shopping.model.repository;

import cs.home.shopping.model.entity.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PromotionRepository extends JpaRepository<Promotion, Long> {

    List<Promotion> findAllByActiveTrueAndRequiresVIP(Boolean requiresVIP);

}
