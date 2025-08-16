package app.modules.inventory.repo;

import app.common.entity.CostPrice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CostPriceRepo extends JpaRepository<CostPrice,Long> {

}
