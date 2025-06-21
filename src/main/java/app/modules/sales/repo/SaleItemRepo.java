package app.modules.sales.repo;

import app.modules.sales.entity.SalesItems;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SaleItemRepo extends JpaRepository<SalesItems,Long> {


}
