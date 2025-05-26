package app.modules.organization.repo;

import app.modules.organization.entity.Branch;
import app.modules.organization.entity.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BranchRepo extends JpaRepository<Branch,Long> {


    boolean existsByNameAndOrg(String name, Organization org);



    boolean existsByNameAndOrgAndIdNotIn(String name, Organization org , List<Long> list);
}
