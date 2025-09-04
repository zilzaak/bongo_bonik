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

    @Query("""
        SELECT p.id as id, p.name as name,
        p.fullName as fullName,
         brand.id as brandId , 
               brand.name as brandName, 
               model.id as modelId , 
               model.name as modelName, cat.id as catId , 
               cat.name as categoryName, 
               mdwth.id as madeWithId , 
               mdwth.name as madeWithName, 
               size.id as sizeId , 
               size.name as sizeName, 
               color.id as colorId , 
               color.name as colorName, org.id as orgId , 
               org.name as orgName ,
               um.id as uomId , 
               um.name as uomName, 
               p.qtyPerUnit  as qtyPerUnit , 
               p.unitName as unitName,
               p.description as description,
               p.createBy as createBy , 
               p.updateBy as updateBy
        FROM Product p 
        JOIN p.org org 
        LEFT JOIN p.brand brand 
        LEFT JOIN p.model model 
        LEFT JOIN p.cat cat 
        LEFT JOIN p.size size 
        LEFT JOIN p.color color 
        LEFT JOIN p.madeWith mdwth 
        left join p.uom um 
        WHERE (:id IS NULL OR p.id = :id)
          AND (:orgId IS NULL OR org.id = :orgId)
          AND (:brandId IS NULL OR brand.id = :brandId)
          AND (:catId IS NULL OR cat.id = :catId)
          AND (:modelId IS NULL OR model.id = :modelId)
          AND ( cast(:searchTerm as string) IS NULL OR UPPER(cast(p.fullName as string)) LIKE CONCAT('%', UPPER(cast(:searchTerm as string)), '%'))
        """)
    Page<Map<String,Object>> getList(
            @Param("id") Long id,
            @Param("orgId") Long orgId,
            @Param("brandId") Long brandId,
            @Param("catId") Long catId,
            @Param("modelId") Long modelId,
            @Param("searchTerm") String searchTerm,
            Pageable pageable
    );

    boolean existsByBrandId(Long id);

    Product findTopByBrandId(Long id);

    boolean existsByCatId(Long id);

    Product findTopByCatId(Long id);

    boolean existsByModelId(Long id);

    Product findTopByModelId(Long id);

    boolean existsByColorId(Long id);

    Product findTopByColorId(Long id);

    boolean existsBySizeId(Long id);

    Product findTopBySizeId(Long id);

    boolean existsByUomId(Long id);

    Product findTopByUomId(Long id);

    boolean existsByMadeWithId(Long id);

    Product findTopByMadeWithId(Long id);

    boolean existsByOrgId(Long id);

    Product findTopByOrgId(Long id);


    @Query("select x.id as id , x.name as name , x.description as description , x.fullName as fullName " +
            " from Product x where x.org.id=:orgId and  " +
            "  cast(x.name as string) like concat('%',:name,'%') and cast(x.criteriaIds as string) like concat('%', cast(:criteriaIds as string),'%' )" +
            "  and ( :id is null or x.id <> :id ) ")
    List<Map<String, Object>> similarProduct(@Param("orgId") Long orgId,
                                             @Param("name") String name,
                                             @Param("criteriaIds") String criteriaIds,
                                             @Param("id") Long id);
}
