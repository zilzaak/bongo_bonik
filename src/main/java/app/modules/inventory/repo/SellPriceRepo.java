package app.modules.inventory.repo;

import app.common.entity.SellPrice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SellPriceRepo extends JpaRepository<SellPrice,Long> {

    boolean existsByProductIdAndOrgIdAndUnitPrice(Long productId, Long orgId, Double unitPrice);

    boolean existsByProductIdAndOrgIdAndUnitPriceAndIdNotIn(Long productId, Long orgId, Double unitPrice, List<Long> list);
}
