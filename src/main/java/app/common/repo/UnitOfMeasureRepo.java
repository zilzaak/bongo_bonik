package app.common.repo;

import app.common.entity.UnitOfMeasure;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UnitOfMeasureRepo extends JpaRepository<UnitOfMeasure,Long> {

    boolean existsByNameAndOrgIdAndIdNotIn(String name, Long orgId, List<Long> list);

    boolean existsByNameAndOrgId(String name, Long orgId);
}
