package app.common.repo;

import app.common.entity.ProductCat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface ProductCatRepo extends JpaRepository<ProductCat,Long> {

    boolean existsByNameAndOrgId(String name, Long orgId);

    boolean existsByNameAndOrgIdAndIdNotIn(String name, Long orgId, List<Long> list);

    @Query("select b.id as id , b.name as categoryName , org.name as orgName , b.created as created  ,  b.updated as updated " +
            " from ProductCat b join Organization org on org.id=b.orgId " +
            " where ( ?1 is null or b.id=?1 ) and " +
            " ( ?2 is null or b.orgId=?2 ) ")
    Page<Map<String, Object>> getList(Long catId, Long orgId, Pageable pageable);
}
