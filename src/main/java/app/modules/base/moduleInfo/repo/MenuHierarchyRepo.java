package app.modules.base.moduleInfo.repo;

import app.modules.base.moduleInfo.entity.MenuHierarchy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface MenuHierarchyRepo extends JpaRepository<MenuHierarchy,Long> {

    boolean existsByApiPatternAndMethodName(String apiPattern, String methodName);

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

    boolean existsByMenuAndApiPatternInAndParentMenuIn(String menu, List<String> asList, List<String> asList1);
}
