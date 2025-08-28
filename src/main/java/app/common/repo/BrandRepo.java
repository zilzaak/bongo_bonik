package app.common.repo;

import app.common.entity.Brand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface BrandRepo extends JpaRepository<Brand,Long> {

    @Query("select count(x) from Brand  x where (   upper(x.name) like concat('%', upper(?1) ,'%')  " +
            " or  upper(?1) like concat('%',upper(x.name),'%')  ) and x.orgId=?2 ")
    int existsByNameAndOrgId(String name, Long orgId);

    @Query("select count(x) from Brand  x where (   upper(x.name) like concat('%', upper(?1) ,'%')  " +
            " or  upper(?1) like concat('%',upper(x.name),'%')  ) and x.orgId=?2 and x.id not in ?3  ")
    int existsByNameAndOrgIdAndIdNotIn(String name, Long orgId, List<Long> asList);


    @Query("select b.id as id , b.name as name , org.name as orgName , b.created as created  , " +
            " b.updated as updated , b.createBy as createBy , b.updateBy as updateBy " +
            " from Brand b join Organization org on org.id=b.orgId " +
            " where ( ?1 is null or b.id=?1 ) and " +
            " ( ?2 is null or b.orgId=?2 ) ")
    Page<Map<String, Object>> getList(Long brandId, Long orgId, Pageable pageable);

    boolean existsByOrgId(Long id);

    Brand findTopByOrgId(Long id);

    boolean existsByOrgIdAndId(Long orgId, Long brandId);
}
