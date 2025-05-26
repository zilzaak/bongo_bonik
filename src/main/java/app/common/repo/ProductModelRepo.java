package app.common.repo;

import app.common.entity.ProductModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductModelRepo extends JpaRepository<ProductModel,Long> {

    boolean existsByNameAndOrgId(String name, Long orgId);

    boolean existsByNameAndOrgIdAndIdNotIn(String name, Long orgId, List<Long> list);
}