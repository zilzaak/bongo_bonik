package app.common.repo;

import app.common.entity.UnitOfMeasure;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

public interface UnitOfMeasureRepo extends JpaRepository<UnitOfMeasure,Long> {
    UnitOfMeasure findTopByOrgId(Long id);

    boolean existsByOrgId(Long id);
    @Query("select b.id as id , b.name as name , b.org.name as orgName , b.created as created   " +
            ",  b.updated as updated, b.createBy as createBy , b.updateBy as updateBy " +
            " from UnitOfMeasure b  " +
            " where ( :id is null or b.id=:id ) and " +
            " ( :orgId is null or b.org.id=:orgId ) and " +
            " cast(:name as String ) is null or upper(cast(b.name as string))  like concat('%',upper(cast(:name as string)),'%') "+
            " ")
    Page<Map<String, Object>> getList(@Param("id")  Long id ,
                                      @Param("orgId") Long orgId,
                                      @Param("name") String name,
                                      Pageable pageable);
}
