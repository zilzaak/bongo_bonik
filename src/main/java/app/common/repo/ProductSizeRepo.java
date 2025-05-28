package app.common.repo;
import app.common.entity.ProductSize;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductSizeRepo extends JpaRepository<ProductSize,Long> {
    boolean existsByName(String name);
    boolean existsByNameAndIdNotIn(String name, List<Long> list);
}
