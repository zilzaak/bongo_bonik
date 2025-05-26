package app.common.repo;

import app.common.entity.ProductColor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductColorRepo extends JpaRepository<ProductColor,Long> {

    boolean existsByNameAndOrgIdAndIdNotIn(String name, Long orgId, List<Long> list);

    boolean existsByNameAndOrgId(String name, Long orgId);
}