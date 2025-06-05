package app.modules.inventory.repo;

import app.modules.inventory.entity.StockBalance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockBalanceRepo extends JpaRepository<StockBalance,Long> {

    StockBalance findByProductIdAndInventoryId(Long productId, Long inventoryId);

}
