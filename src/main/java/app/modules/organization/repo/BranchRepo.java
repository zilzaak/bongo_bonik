package app.modules.organization.repo;

import app.modules.organization.entity.Branch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BranchRepo extends JpaRepository<Branch,Long> {


}
