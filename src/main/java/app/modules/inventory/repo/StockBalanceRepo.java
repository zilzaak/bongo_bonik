package app.modules.inventory.repo;

import app.modules.inventory.entity.StockBalance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface StockBalanceRepo extends JpaRepository<StockBalance,Long> {

    StockBalance findByProductIdAndInventoryId(Long productId, Long inventoryId);

    @Query("select x.quantity from StockBalance x where x.productId=:productId and x.inventoryId=:inventory")
    Optional<Integer> stockQbalanceOfProduct(@Param("productId") Long productId , @Param("inventory")  Long inventory);
}
