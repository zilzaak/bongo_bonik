package app.modules.inventory.repo;

import app.common.entity.SellPrice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SellPriceRepo extends JpaRepository<SellPrice,Long> {

    boolean existsByProductIdAndPrice(Long productId, Double unitPrice);

    boolean existsByProductIdAndPriceAndIdNotIn(Long productId, Double unitPrice, List<Long> list);
}
