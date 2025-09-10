package app.common.repo;

import app.common.entity.ProductCat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

public interface ProductCatRepo extends JpaRepository<ProductCat,Long> {

    @Query("select count(x) from ProductCat  x where cast(x.name as string)=cast(:name as string) and x.orgId=:org ")
    int existsByNameAndOrgId(@Param("name") String name, @Param("org")  Long org);

    @Query("select count(x) from ProductCat  x where cast(x.name as string)=cast(:name as string) and x.orgId=:org  and x.id not in :ids ")
    int existsByNameAndOrgIdAndIdNotIn(@Param("name") String name, @Param("org")  Long org ,  @Param("ids") List<Long> ids);


    @Query("select b.id as id , b.name as name , org.name as orgName , b.created as created   " +
            ",  b.updated as updated, b.createBy as createBy , b.updateBy as updateBy , b.description as description " +
            " from ProductCat b join Organization org on org.id=b.orgId " +
            " where ( ?1 is null or b.id=?1 ) and " +
            " ( ?2 is null or b.orgId=?2 ) and " +
            " cast(?3 as String ) is null or cast(b.name as string) like concat('%',upper(cast(?3 as string)),'%') "+
            " ")
    Page<Map<String, Object>> getList(Long catId, Long orgId,String name, Pageable pageable);

    boolean existsByOrgId(Long id);

    ProductCat findTopByOrgId(Long id);

    boolean existsByOrgIdAndId(Long orgId, Long catId);

    @Query("select b.id as id , b.name as name , b.created as created  , " +
            " b.updated as updated , b.description as description " +
            " from ProductCat b join Organization org on org.id=b.orgId " +
            " where org.id=:orgId  and " +
            "  upper(cast(b.name as string)) like concat('%', upper(cast(:name as string)) ,'%') and" +
            " ( :id is null or b.id <> :id ) ")
    List<Map<String, Object>> existData(@Param("orgId") Long orgId,
                                        @Param("name") String name,
                                        @Param("id") Long id);
}
