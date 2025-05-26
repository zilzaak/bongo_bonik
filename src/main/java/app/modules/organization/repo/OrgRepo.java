package app.modules.organization.repo;

import app.modules.organization.entity.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrgRepo extends JpaRepository<Organization,Long> {


}
