package app.common.repo;
import app.common.entity.ProductSize;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductSizeRepo extends JpaRepository<ProductSize,Long> {

    boolean existsByNameAndOrgIdAndIdNotIn(String name, Long orgId, List<Long> list);

    boolean existsByNameAndOrgId(String name, Long orgId);
}
