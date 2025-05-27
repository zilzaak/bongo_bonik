package app.common.repo;

import app.common.entity.UnitOfMeasure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UnitOfMeasureRepo extends JpaRepository<UnitOfMeasure,Long> {

    boolean existsByNameAndOrgIdAndIdNotIn(String name, Long orgId, List<Long> list);

    boolean existsByNameAndOrgId(String name, Long orgId);

    @Query("select x.productCatIds from UnitOfMeasure x where x.name=:name and x.orgId=:orgId ")
    List<String> getExistCat(@Param("name") String name, @Param("orgId") Long orgId);
    @Query("select x.productCatIds from UnitOfMeasure x where x.name=:name and x.orgId=:orgId and x.id <> :id ")
    List<String> getExistCatExceptId(@Param("name") String name, @Param("orgId") Long orgId,  @Param("id")  Long id);
}
