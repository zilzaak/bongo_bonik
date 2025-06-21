package app.modules.sales.repo;

import app.modules.sales.entity.Sales;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SaleRepo extends JpaRepository<Sales,Long> {


}
