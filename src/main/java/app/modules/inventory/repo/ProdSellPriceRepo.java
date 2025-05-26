package app.modules.inventory.repo;

import app.modules.inventory.entity.ProdSellPrice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdSellPriceRepo extends JpaRepository<ProdSellPrice,Long> {

}
