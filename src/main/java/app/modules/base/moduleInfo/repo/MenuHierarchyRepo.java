package app.modules.base.moduleInfo.repo;

import app.modules.base.moduleInfo.entity.MenuHierarchy;
import app.modules.base.urlPerm.dto.MenuData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

public interface MenuHierarchyRepo extends JpaRepository<MenuHierarchy,Long> {
    @Query("SELECT COUNT(x) from MenuHierarchy x where x.apiPattern=?1  " +
            " and x.id not in ?2 ")
    int existPatternOrName(String apiPattern, List<Long> asList);

    @Query("SELECT x.id as id , x.apiPattern as apiPattern , " +
            "  x.methodName as methodName , " +
            " x.frontUrl as frontUrl , " +
            " x.menu as moduleName , concat(x.apiSeq,'-',x.menu ) as ddlCode ," +
            " x.parentMenu as parentModule , x.apiSeq as apiSeq  " +
            "  from MenuHierarchy x " +
            "  where ( :menu is null or cast(x.menu as String) like concat('%', cast(:menu as String)  ,'%' ) ) and ( :mid is null or x.id=:mid )")
    Page<Map<String,Object>> getList(@Param("mid") Long mid , @Param("menu") String menu , Pageable pageable);
    boolean existsByApiPattern(String backendUrl);
    boolean existsByFrontUrl(String frontUrl);

    MenuHierarchy findByApiPattern(String backendUrl);

    MenuHierarchy findTopByMenu(String parentMenu);

    @Query(value="SELECT x.id as id , x.parent_id as parentId , x.menu as menu , x.method_name as methodName " +
            "FROM menu_hierarchy x " +
            "WHERE x.id IN ?1 ",nativeQuery = true)
    List<Map<String,Object>> getMenuNames(List<Long> menuIds);

    boolean existsByApiSeqAndMenu(String apiSeq, String menu);

    boolean existsByApiSeqAndMenuAndIdNotIn(String apiSeq, String menu, List<Long> asList);

    boolean existsByParentMenu(String parentMenu);

    boolean existsByMenu(String menu);

    boolean existsByMenuAndIdNotIn(String menu, List<Long> asList);

    MenuHierarchy findByMenuAndApiSeq(String parentMenu, String parentApiSeq);
    @Query("SELECT x FROM MenuHierarchy x " +
            "WHERE x.apiPattern is not null ")
    List<MenuHierarchy> getAllUrl();

    @Query(value = "SELECT parent_id FROM menu_hierarchy WHERE id = :id", nativeQuery = true)
    Long findParentIdById(@Param("id") Long id);
    @Query("SELECT menu FROM MenuHierarchy WHERE id = ?1 ")
    String getName(Long menuId);

    @Query("SELECT new app.modules.base.urlPerm.dto.MenuData(x.frontUrl  , " +
            "  x.menu  , " +
            " x.parentMenu  , " +
            " x.apiPattern , " +
            " x.methodName , " +
            " x.apiSeq )  " +
            "  from MenuHierarchy x " +
            "  where x.id=?1 ")
    MenuData getData(Long id);
}
