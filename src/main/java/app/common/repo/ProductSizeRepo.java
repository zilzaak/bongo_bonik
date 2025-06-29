package app.common.repo;
import app.common.entity.ProductSize;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface ProductSizeRepo extends JpaRepository<ProductSize,Long> {
    boolean existsByName(String name);
    boolean existsByNameAndIdNotIn(String name, List<Long> list);

    @Query("select b.id as id , b.name as sizeName , b.orgName as orgName , b.created as created  ,  b.updated as updated " +
            " from ProductSize b " +
            " where ( ?1 is null or b.id=?1 ) and " +
            " ( ?2 is null or b.orgId=?2 ) ")
    Page<Map<String, Object>> getList(Long sizeId, Long orgId, Pageable pageable);

    boolean existsByOrgId(Long id);

    ProductSize findTopByOrgId(Long id);
}
