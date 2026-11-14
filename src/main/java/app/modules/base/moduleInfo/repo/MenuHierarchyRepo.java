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
            "  x.methodName as methodName ,x.menu as  menuCode ," +
            "  x.created as created ,  " +
            "  x.updated as updated ,"+
            " x.frontUrl as frontUrl , " +
            " x.menu as moduleName , concat(x.apiPattern,'-',x.menu ) as urlCode ," +
            " x.parentMenu as parentModule , x.apiPattern as apiSeq , " +
            " x.sidebarElement as sidebarElement " +
            "  from MenuHierarchy x " +
            "  where ( :menu is null or   cast( x.menu as String)  like  concat('%',cast(:menu as String) ,'%')) " +
            "  and ( :url is null or   cast( x.apiPattern as String)  like  concat('%',cast(:url as String) ,'%'))"+
            "  and ( :mid is null or x.id=:mid )  " +
            "  and ( :loadMethod is null or x.methodName is not null  ) " +
            "  and ( :loadMenu is null or x.methodName is null ) " )
    Page<Map<String,Object>> getList(@Param("mid") Long mid ,
                                     @Param("menu") String menu ,
                                     @Param("url") String url ,
                                     @Param("loadMethod") String loadMethod ,
                                     @Param("loadMenu") String loadMenu ,
                                     Pageable pageable);

    @Query("select x.id as id,  " +
            " x.apiPattern as apiPattern, " +
            "  x.created as created ,  " +
            "  x.updated as updated ,"+
            "x.methodName as methodName, " +
            "x.frontUrl as frontUrl, " +
            "x.menu as moduleName, " +
            "x.parentMenu as parentModule , " +
            " x.sidebarElement as sidebarElement " +
            " from MenuHierarchy x " +
            " left join x.parent parent " +
            "where (:parent is null or parent.id = :parent) " +
            "and (:backendUrlId is null or x.id = :backendUrlId) " +
            "and (cast(:frontendUrl as text) is null or cast(x.frontUrl as text) = cast(:frontendUrl as text)) " +
            "order by x.id desc")
    Page<Map<String, Object>> getListParent(@Param("parent") Long parent,
                                            @Param("backendUrlId") Long backendUrlId,
                                            @Param("frontendUrl") String frontendUrl,
                                            Pageable pageable);

    boolean existsByApiPattern(String backendUrl);


    @Query(value="SELECT x.id as id , x.parent_id as parentId , x.menu as menu , x.method_name as methodName " +
            "FROM menu_hierarchy x " +
            "WHERE x.id IN ?1 ",nativeQuery = true)
    List<Map<String,Object>> getMenuNames(List<Long> menuIds);

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
            " x.methodName, " +
            " x.sidebarElement )  " +
            "  from MenuHierarchy x " +
            "  where x.id=?1 ")
    MenuData getData(Long id);


    @Query(" select count(x.id) " +
            "  from MenuHierarchy x " +
            "  where ( ?1 is null or x.menu = ?1 ) " +
            "  and ( ?2 is null or x.parent.id = ?2 )")
    int duplicateMenuCheck(String menu, Long parentId);

    @Query(" select count(x.id) " +
            "  from MenuHierarchy x " +
            "  where ( ?1 is null or x.menu = ?1 ) " +
            "  and ( ?2 is null or x.parent.id = ?2 ) " +
            " and x.id <> ?3 ")
    int duplicateMenuCheckEdit(String menu, Long parentId , Long id);
}
