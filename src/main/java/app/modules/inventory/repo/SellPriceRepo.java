package app.modules.inventory.repo;

import app.common.entity.Product;
import app.common.entity.SellPrice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SellPriceRepo extends JpaRepository<SellPrice,Long> {

    boolean existsByProductIdAndDefaultSellPrice(Long productId, Double unitPrice);

    boolean existsByProductIdAndDefaultSellPriceAndIdNotIn(Long productId, Double unitPrice, List<Long> list);

    SellPrice findByProduct(Product product);

    SellPrice findByProductId(Long productId);
}
