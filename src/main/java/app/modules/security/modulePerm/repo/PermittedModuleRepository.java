package app.modules.security.modulePerm.repo;

import app.modules.security.entity.Role;
import app.modules.security.entity.User;
import app.modules.security.modulePerm.entity.PermittedModule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PermittedModuleRepository extends JpaRepository<PermittedModule,Long> {

    boolean existsByModuleIdAndUser(Long moduleId, User user);

    boolean existsByModuleIdAndUserAndIdNotIn(Long moduleId, User user, List<Long> asList);

    boolean existsByModuleIdAndRole(Long moduleId, Role role);

    boolean existsByModuleIdAndRoleAndIdNotIn(Long moduleId, Role role, List<Long> asList);
}
