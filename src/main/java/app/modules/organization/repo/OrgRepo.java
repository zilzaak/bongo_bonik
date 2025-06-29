package app.modules.organization.repo;

import app.modules.organization.entity.Organization;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

public interface OrgRepo extends JpaRepository<Organization,Long> {


    @Query("select x.name from Organization x where x.id=:id")
    String getName(@Param("id") Long id);

    boolean existsByName(String name);

    boolean existsByNameAndIdNotIn(String name, List<Long> list);

    @Query("select b.name as orgName , b.phone as orgPhone , " +
            "  b.address as orgAddress , b.location as orgLocation , " +
            "  b.created as created  " +
            " from Organization b " +
            " where ( ?1 is null or b.id=?1 ) and " +
            "  ( ?2 is null or upper(b.name) like concat('%', upper(?2),'%') or " +
            "  upper(b.location) like concat('%', upper(?2),'%') or " +
            "  upper(b.address)  like concat('%', upper(?2),'%') or " +
            "  b.phone like concat('%', ?2,'%') ) ")
    Page<Map<String, Object>> getList(Long orgId, String commonField, Pageable pageable);
}
