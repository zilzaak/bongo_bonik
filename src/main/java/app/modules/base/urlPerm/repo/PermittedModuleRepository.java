package app.modules.base.urlPerm.repo;

import app.modules.base.role.entity.Role;
import app.modules.base.user.entity.User;
import app.modules.base.urlPerm.entity.PermittedModule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface PermittedModuleRepository extends JpaRepository<PermittedModule,Long> {

    boolean existsByModuleIdAndUser(Long moduleId, User user);

    boolean existsByModuleIdAndUserAndIdNotIn(Long moduleId, User user, List<Long> asList);

    boolean existsByModuleIdAndRole(Long moduleId, Role role);

    boolean existsByModuleIdAndRoleAndIdNotIn(Long moduleId, Role role, List<Long> asList);


}
