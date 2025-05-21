package app.common.repo;

import app.common.entity.ProductCat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductCatRepo extends JpaRepository<ProductCat,Long> {

}
