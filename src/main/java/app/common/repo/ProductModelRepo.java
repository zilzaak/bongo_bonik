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


    @Query("select b.id as id , b.name as name , b.brand.id as brandId , " +
            " b.brand.name as brandName , b.org.name as orgName , " +
            " b.created as created  ,  b.updated as updated " +
            " , b.createBy as createBy , b.updateBy as updateBy " +
            " from ProductModel b " +
            " where ( ?1 is null or b.brand.id=?1 )  " +
            " and ( ?2 is null or b.org.id=?2 ) " +
            " and ( ?3 is null or b.id=?3)  " +
            " and  cast(?4 as String) is null or cast(b.name as string) like concat('%',upper(cast(?4 as string)),'%') ")
    Page<Map<String, Object>> getList(Long brandId, Long orgId, Long id ,String name ,Pageable pageable);

    boolean existsByOrgId(Long id);

    ProductModel findTopByOrgId(Long id);

    boolean existsByOrgIdAndId(Long orgId, Long modelId);

    ProductModel findTopByBrandId(Long id);

    boolean existsByBrandId(Long id);
}