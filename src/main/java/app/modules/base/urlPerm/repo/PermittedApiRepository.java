package app.modules.base.urlPerm.repo;

import app.modules.base.role.entity.Role;
import app.modules.base.urlPerm.entity.PermittedApi;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface PermittedApiRepository extends JpaRepository<PermittedApi,Long> {

    boolean existsByRoleIdAndUserIdAndBackendUrl(Long role,Long user, String backendUrl);

    boolean existsByRoleIdAndUserIdAndBackendUrlAndIdNotIn(Long role, Long user , String backendUrl, List<Long> asList);

    @Query("select menu.menu as menu , " +
            " x.id as id , " +
            " x.backendUrl as backendUrl , " +
            " x.frontendUrl as frontendUrl ," +
            " menu.methodName as methodName " +
            " from PermittedApi x " +
            " left join x.user user " +
            " left join x.role role  " +
            " left join MenuHierarchy menu on menu.id=x.menuId " +
            " where ( ?1 is null or concat(',',x.menuIdsHierarchy,',') like concat('%,', ?1 , ',%') )  " +
            " and ( ?2 is null or user.id=?2 ) " +
            " and ( ?3 is null or role.id=?3 )")
    Page<Map<String, Object>> getList(String menuId ,
                                      Long userId ,
                                      Long roleId,
                                      Pageable pageable);


    @Query("select  x from PermittedApi x " +
            " left join x.user user " +
            " left join x.role role  " +
            " where ( ?1 is null or user.id=?1 ) " +
            " and ( ?2 is null or role in ?2  ) ")
    List<PermittedApi> getPermittedApis(Long userid , Set<Role> roles);

    boolean existsByMenuId(Long id);
}
