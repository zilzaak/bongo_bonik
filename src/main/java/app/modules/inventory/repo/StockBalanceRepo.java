package app.modules.inventory.repo;

import app.modules.inventory.entity.StockBalance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface StockBalanceRepo extends JpaRepository<StockBalance,Long> {

    StockBalance findByProductIdAndInventoryId(Long productId, Long inventoryId);

    @Query("select x.quantity from StockBalance x where x.productId=:productId and x.inventoryId=:inventory")
    Optional<Integer> stockQbalanceOfProduct(@Param("productId") Long productId , @Param("inventory")  Long inventory);

    @Modifying
    @Query("UPDATE StockBalance sb SET sb.quantity = sb.quantity - :subQty WHERE sb.productId = :productId AND sb.inventoryId = :inventoryId")
    int deductStock(@Param("productId") Long productId,
                    @Param("inventoryId") Long inventoryId,
                    @Param("subQty") Integer subQty);


    @Modifying
    @Query("UPDATE StockBalance sb SET sb.quantity = sb.quantity + :addQty WHERE sb.productId = :productId AND sb.inventoryId = :inventoryId")
    int addStock(@Param("productId") Long productId,
                    @Param("inventoryId") Long inventoryId,
                    @Param("addQty") Integer addQty);

    boolean existsByInventoryIdAndProductIdAndQuantityLessThan(Long inventoryId, Long id, Integer quantity);

    boolean existsByInventoryId(Long id);

    StockBalance findTopByInventoryId(Long id);
}
