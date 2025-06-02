package app.common.counter.repo;

import app.common.counter.entity.SystemCounter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SystemCounterRepo extends JpaRepository<SystemCounter,Long> {
    SystemCounter findByNameAndOrgIdAndBranchId(String counterName, Long orgId, Long branchId);
}
