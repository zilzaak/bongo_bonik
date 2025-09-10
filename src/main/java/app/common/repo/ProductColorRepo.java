package app.common.repo;

import app.common.entity.ProductColor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

public interface ProductColorRepo extends JpaRepository<ProductColor,Long> {

    boolean existsByNameAndOrgIdAndIdNotIn(String name, Long orgId, List<Long> list);

    boolean existsByNameAndOrgId(String name, Long orgId);

    @Query("select b.id as id , b.name as name , b.orgName as orgName , b.created as created  ,  b.updated as updated " +
            " , b.createBy as createBy , b.updateBy as updateBy,b.description as description  from ProductColor b  " +
            " where ( ?1 is null or b.id=?1 ) and " +
            " ( ?2 is null or b.orgId=?2 ) ")
    Page<Map<String, Object>> getList(Long colorId, Long orgId, Pageable pageable);

    boolean existsByOrgId(Long id);

    ProductColor findTopByOrgId(Long id);

    boolean existsByOrgIdAndId(Long orgId, Long colorId);
    @Query("select b.id as id , b.name as name , b.created as created  , " +
            " b.updated as updated , b.description as description " +
            " from ProductColor b join Organization org on org.id=b.orgId" +
            " where org.id=:orgId  and " +
            " upper(cast(b.name as string)) like concat('%', upper(cast(:name as string)) ,'%') and" +
            " ( :id is null or b.id <> :id ) ")
    List<Map<String, Object>> existData(@Param("orgId") Long orgId,
                                        @Param("name") String name,
                                        @Param("id") Long id);
}