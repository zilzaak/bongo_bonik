package app.modules.inventory.repo;

import app.common.entity.Product;
import app.common.entity.Pricing;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SellPriceRepo extends JpaRepository<Pricing,Long> {

    boolean existsByProductIdAndDefaultSellPrice(Long productId, Double unitPrice);

    boolean existsByProductIdAndDefaultSellPriceAndIdNotIn(Long productId, Double unitPrice, List<Long> list);

    Pricing findByProduct(Product product);

    Pricing findByProductId(Long productId);
}
