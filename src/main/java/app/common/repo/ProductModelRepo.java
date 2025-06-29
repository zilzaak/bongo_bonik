package app.common.repo;

import app.common.entity.ProductModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface ProductModelRepo extends JpaRepository<ProductModel,Long> {

    boolean existsByNameAndBrandId(String name, Long brandId);

    boolean existsByNameAndBrandIdAndIdNotIn(String name, Long brandId, List<Long> list);


    @Query("select b.id as id , b.brandName as brandName , b.orgName as orgName , b.created as created  ,  b.updated as updated " +
            " from ProductModel b " +
            " where ( ?1 is null or b.brandId=?1 )  " +
            " and ( ?2 is null or b.orgId=?2 ) " +
            " and ( ?3 is null or b.id=?3) ")
    Page<Map<String, Object>> getList(Long brandId, Long orgId, Long id ,Pageable pageable);

    boolean existsByOrgId(Long id);

    ProductModel findTopByOrgId(Long id);

    boolean existsByOrgIdAndId(Long orgId, Long modelId);
}