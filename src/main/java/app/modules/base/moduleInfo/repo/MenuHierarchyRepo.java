package app.modules.base.moduleInfo.repo;

import app.modules.base.moduleInfo.entity.MenuHierarchy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface MenuHierarchyRepo extends JpaRepository<MenuHierarchy,Long> {
    @Query("SELECT COUNT(x) from MenuHierarchy x where x.apiPattern=?1  " +
            " and x.id not in ?2 ")
    int existPatternOrName(String apiPattern, List<Long> asList);

    @Query("SELECT x.apiPattern as apiPattern , " +
            "  x.methodName as methodName , " +
            " x.frontUrl as frontUrl , " +
            " x.menu as moduleName " +
            "  from MenuHierarchy x " +
            "  where x.id=?1 ")
    Page<Map<String,Object>> getList(Long moduleId , Pageable pageable);
    boolean existsByApiPattern(String backendUrl);
    boolean existsByFrontUrl(String frontUrl);

    MenuHierarchy findByApiPattern(String backendUrl);

    MenuHierarchy findTopByMenu(String parentMenu);

    @Query("SELECT x.id as id , x.menu as menu , x.methodName as methodName " +
            "FROM MenuHierarchy x " +
            "WHERE x.id IN ?1 ")
    List<Map<String,Object>> getMenuNames(List<Long> menuIds);

    boolean existsByApiSeqAndParentMenu(String apiSeq, String parentMenu);

    boolean existsByApiSeqAndParentMenuAndIdNotIn(String apiSeq, String parentMenu, List<Long> asList);

    boolean existsByParentMenu(String parentMenu);

    boolean existsByParentMenuAndIdNotIn(String parentMenu, List<Long> asList);

    MenuHierarchy findByMenuAndApiSeq(String parentMenu, String parentApiSeq);
    @Query("SELECT x FROM MenuHierarchy x " +
            "WHERE x.apiPattern is not null ")
    List<MenuHierarchy> getAllUrl();
}
