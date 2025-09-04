package app.modules.base.urlPerm.repo;

import app.modules.base.role.entity.Role;
import app.modules.base.urlPerm.entity.PermittedApi;
import app.modules.base.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface PermittedApiRepository extends JpaRepository<PermittedApi,Long> {
    boolean existsByRoleIdAndUserIdAndBackendUrlAndMenuId(Long role,Long user, String backendUrl , Long menuId);
    boolean existsByRoleIdAndUserIdAndBackendUrlAndAndMenuIdAndIdNotIn(Long role, Long user , String backendUrl, Long menuId,List<Long> asList);

    @Query("select menu.menu as menu , " +
            " menu.id as menuId , " +
            " x.id as id , " +
            " x.backendUrl as backendUrl , " +
            " menu.methodName as methodName, " +
            " user.username as username , " +
            " role.id as roleId , user.id as userId , " +
            " role.authority as authority , " +
            " x.created as created , " +
            " x.updated as updated  " +
            " from PermittedApi x " +
            " left join x.user user " +
            " left join x.role role " +
            " left join MenuHierarchy menu on menu.id=x.menuId " +
            " where ( :mid is null or concat(',',cast(x.menuIdsHierarchy as string),',') like concat('%,', cast(:mid as string) , ',%') ) " +
            " and ( :parentMenuId is null or concat(',',cast(x.menuIdsHierarchy as string),',') like concat('%,', cast(:parentMenuId as string) , ',%') )  " +
            " and ( :uid is null or user.id=:uid ) " +
            " and ( :rid is null or role.id=:rid ) " +
            " and ( :id is null or x.id=:id ) order by x.backendUrl asc")
    Page<Map<String, Object>> getList(
                                      @Param("id") Long id,
                                      @Param("parentMenuId") String parentMenuId,
                                      @Param("mid") String mid,
                                      @Param("uid")Long uid ,
                                      @Param("rid")Long rid,
                                      Pageable pageable);


    @Query("select  x from PermittedApi x " +
            " left join x.user user " +
            " left join x.role role  " +
            " where user=:ud  " +
            " or role in :rl order by x.id asc")
    List<PermittedApi> getPermittedApis(@Param("ud") User ud ,
                                        @Param("rl") Set<Role> rl);

    boolean existsByMenuId(Long id);
    @Query("select x.backendUrl as apiPattern , r.authority as authority , u.username as username " +
            " from PermittedApi x " +
            " left join x.role r  " +
            " left join x.user u  " +
            " where  " +
            "  r in :role order by x.id desc ")
    List<Map<String, Object>> getUsersPermittedMenu(@Param("role") Set<Role> role );


    @Query("select x.backendUrl as apiPattern , r.authority as authority , u.username as username  " +
            " from PermittedApi x " +
            " left join x.role r  " +
            " left join x.user u  " +
            " where  " +
            "  u.id=:userid  order by x.id desc")
    List<Map<String, Object>> getUsersPermittedMenu(@Param("userid") Long userid  );

    @Query("select x.backendUrl as apiPattern , r.authority as authority , u.username as username  " +
            " from PermittedApi x " +
            " left join x.role r  " +
            " left join x.user u  " +
            " where  x.backendUrl is not null and  " +
            "  u=:userid " +
            "  or r in :role  order by x.id desc ")
    List<Map<String, Object>> getUsersPermittedMenu(@Param("userid") User userid , @Param("role") Set<Role> role );

    PermittedApi findByMenuId(Long id);

    int countByRole(Role role);
}
