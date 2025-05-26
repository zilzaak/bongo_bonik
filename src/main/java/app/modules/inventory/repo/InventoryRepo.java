package app.modules.inventory.repo;

import app.modules.inventory.entity.ProdSellPrice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryRepo extends JpaRepository<ProdSellPrice,Long> {


}
