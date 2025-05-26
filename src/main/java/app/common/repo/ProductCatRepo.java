package app.common.repo;

import app.common.entity.ProductCat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductCatRepo extends JpaRepository<ProductCat,Long> {

    boolean existsByNameAndOrgId(String name, Long orgId);

    boolean existsByNameAndOrgIdAndIdNotIn(String name, Long orgId, List<Long> list);
}
