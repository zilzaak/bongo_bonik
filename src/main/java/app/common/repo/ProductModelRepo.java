package app.common.repo;

import app.common.entity.ProductModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductModelRepo extends JpaRepository<ProductModel,Long> {

}