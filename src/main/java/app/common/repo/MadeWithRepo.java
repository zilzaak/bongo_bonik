package app.common.repo;

import app.common.entity.MadeWith;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MadeWithRepo extends JpaRepository<MadeWith,Long> {


    boolean existsByNameAndOrgIdAndIdNotIn(String name, Long orgId, List<Long> list);

    boolean existsByNameAndOrgId(String name, Long orgId);
}
