package app.modules.inventory.repo;

import app.common.entity.CostPrice;
import app.common.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CostPriceRepo extends JpaRepository<CostPrice,Long> {

    CostPrice findByProduct(Product product);
}
