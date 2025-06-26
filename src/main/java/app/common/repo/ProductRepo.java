package app.common.repo;

import app.common.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

public interface ProductRepo extends JpaRepository<Product,Long> {

    boolean existsByOrgIdAndFullName(Long orgId, String fullName);

    boolean existsByOrgIdAndFullNameAndIdNotIn(Long orgId, String fullName, List<Long> list);

    @Query("select p.name from Product p where p.id=:pid ")
    String getProductName(@Param("pid") Long pid);

    @Query("select p from Product p where ( ?1 is null or p.id=?1 ) " +
            " and ( ?2 is null or p.brand.id=?2 ) " +
            " and ( ?3 is null or p.)")
    Page<Map<String, Object>> getList(Long productId,
                                      Long orgId,
                                      Long brandId,
                                      Long catId,
                                      Long modelId,
                                      Long sizeId,
                                      Long colorId,
                                      Pageable pageable);
}
