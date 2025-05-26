package app.common.repo;

import app.common.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BrandRepo extends JpaRepository<Brand,Long> {

    boolean existsByNameAndOrgId(String name, Long orgId);

    boolean existsByNameAndOrgIdAndIdNotIn(String name, Long orgId, List<Long> asList);
}
