package app.modules.moduleInfo.repo;

import app.modules.moduleInfo.entity.ApiAgainstModule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface ApiAgainstModuleRepo extends JpaRepository<ApiAgainstModule,Long> {

    boolean existsByApiPatternAndMethodName(String apiPattern, String methodName);

    @Query("SELECT COUNT(x) from ApiAgainstModule x where x.apiPattern=?1  " +
            " and x.id not in ?2 ")
    int existPatternOrName(String apiPattern, List<Long> asList);

    @Query("SELECT x.apiPattern as apiPattern , " +
            "  x.methodName as methodName , " +
            " x.frontUrl as frontUrl , " +
            " info.name as moduleName " +
            "  from ApiAgainstModule x " +
            " left join  x.moduleInfo info  " +
            "  where info.id=?1 ")
    Page<Map<String,Object>> getList(Long moduleId , Pageable pageable);
}
