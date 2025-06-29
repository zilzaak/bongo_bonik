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
            " inner join Organization org on org.id=p.orgId   " +
            " left join p.brand brand " +
            " left join p.model model " +
            " left join ProductCat cat on p.catId=cat.id " +
            " left join ProductSize size on p.sizeId = size.id " +
            " left join ProductColor color on p.colorId=color.id " +
            " left join MadeWith mdwth on mdwth.id=p.madeWithId  " +
            " where ( ?1 is null or p.id=?1 ) " +
            " and ( ?2 is null or p.orgId= ?2 ) " +
            " and ( ?3 is null or brand.id=?3 )  " +
            " and ( ?4 is null or p.catId= ?4 ) " +
            " and  (?5 is null or model.id= ?5 )  " +
            " and ( ?6 is null or p.sizeId=?6 )  " +
            " and  ( ?7 is null or p.colorId= ?7 ) " )
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
}
