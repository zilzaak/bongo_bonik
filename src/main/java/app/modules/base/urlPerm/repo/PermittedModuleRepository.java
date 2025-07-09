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
     @Query("select x.moduleId as moduleId , " +
             " module.name as moduleName , " +
             " apiAgnstMdle.apiPattern as apiPattern , " +
             " apiAgnstMdle.methodName as methodName " +
             " from PermittedModule x   " +
             " join x.details dtl " +
             " join dtl.api apiAgnstMdle  " +
             " join apiAgnstMdle.moduleInfo  module  " +
             "  where :user is not null and ( x.user=:user or x.role in :roles ) " +
             " group by  x.moduleId , module.name , apiAgnstMdle.apiPattern  ")
    List<Map<String,Object>> getMenu(@Param("user") User user, @Param("roles") Set<Role> roles);

    @Query("select x.moduleId as moduleId , " +
            " module.name as moduleName , " +
            " apiUnderMdl.apiPattern as apiPattern , " +
            " apiUnderMdl.methodName as methodName , " +
            " concat(user.id,'-',user.username) as user , " +
            " concat(role.id,'-',role.authority) as role " +
            " from PermittedModule x   " +
            " left join PermittedApi dtl on dtl.permittedModule=x " +
            " left join dtl.api apiUnderMdl  " +
            " left join apiUnderMdl.moduleInfo  module  " +
            " left join x.user user  " +
            " left join x.role role " +
            "  where ( :moduleId is null or x.moduleId=:moduleId ) " +
            " and ( :username is null or user.username=:username )  " +
            " and ( :roleId is null or role.id=:roleId ) " +
            " and ( :apiPattern is null or apiUnderMdl.apiPattern=:apiPattern ) " +
            " group by  x.moduleId , module.name , apiUnderMdl.apiPattern , apiUnderMdl.methodName  ")
    Page<Map<String, Object>> getList(@Param("moduleId")   Long moduleId,
                                      @Param("username")   String username,
                                      @Param("roleId")     Long roleId,
                                      @Param("apiPattern") String apiPattern,
                                      Pageable pageable);
}
