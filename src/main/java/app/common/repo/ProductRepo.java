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

    @Query("select p.id as id , " +
            " p.name as productName , " +
            " brand.name as brandName , " +
            " model.name as modelName , " +
            " cat.name as categoryName ," +
            " mdwth.name as madeWithName , " +
            " size.name as sizeName , " +
            " color.name as colorName , " +
            " org.name as orgName   " +
            " from Product p " +
            " join p.org org  " +
            " left join p.brand brand " +
            " left join p.model model " +
            " left join  p.cat  cat " +
            " left join p.size size" +
            " left join p.color  color " +
            " left join p.madeWith mdwth  " +
            " where ( ?1 is null or p.id=?1 ) " +
            " and ( ?2 is null or org.id= ?2 ) " +
            " and ( ?3 is null or brand.id=?3 )  " +
            " and ( ?4 is null or cat.id= ?4 ) " +
            " and  (?5 is null or model.id= ?5 )  " +
            " and ( ?6 is null or size.id=?6 )  " +
            " and  ( ?7 is null or color.id= ?7 ) " )
    Page<Map<String, Object>> getList(Long productId,
                                      Long orgId,
                                      Long brandId,
                                      Long catId,
                                      Long modelId,
                                      Long sizeId,
                                      Long colorId,
                                      Pageable pageable);

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
            "  cast(x.name as string) like concat('%',:name,'%') and x.criteriaIds=:criteriaIds ")
    List<Map<String, Object>> similarProduct(@Param("orgId") Long orgId,
                                             @Param("name") String name,
                                             @Param("criteriaIds") String criteriaIds);

    @Query("select x.id as id , x.name as name , x.description as description , x.fullName as fullName " +
            " from Product x where x.org.id=:orgId and  " +
            "  cast(x.name as string) like concat('%',:name,'%') and x.criteriaIds=:criteriaIds and x.id <> :id ")
    List<Map<String, Object>> similarProduct(@Param("orgId") Long orgId,
                                             @Param("name") String name,
                                             @Param("criteriaIds") String criteriaIds,
                                             @Param("id") Long id);
}
