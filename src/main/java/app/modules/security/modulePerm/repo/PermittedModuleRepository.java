package app.modules.security.modulePerm.repo;

import app.modules.security.entity.Role;
import app.modules.security.entity.User;
import app.modules.security.modulePerm.entity.PermittedModule;
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
     @Query("select module.name as moduleName , apiAgnstMdle.apiPattern as apiPattern from PermittedModule x   " +
             " join x.details dtl " +
             " join dtl.api apiAgnstMdle  " +
             " join apiAgnstMdle.moduleInfo as module  " +
             "  where :user is not null and ( x.user=:user or x.role in :roles ) " +
             " group by  module.name , apiAgnstMdle.apiPattern  ")
    List<Map<String,Object>> getMenu(@Param("user") User user, @Param("roles") Set<Role> roles);
}
