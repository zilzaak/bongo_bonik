package app.common.repo;

import app.common.entity.ProductColor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductColorRepo extends JpaRepository<ProductColor,Long> {

}