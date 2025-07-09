package app.modules.base.role.repo;

import app.modules.base.role.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Long> {

    boolean existsByAuthority(String authority);

    boolean existsByAuthorityAndIdNotIn(String authority, List<Long> asList);

    Role findByAuthority(String authority);
}
