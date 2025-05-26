package app.modules.inventory.repo;

import app.modules.inventory.entity.ProdCostPrice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdCostPriceRepo extends JpaRepository<ProdCostPrice,Long> {

}
