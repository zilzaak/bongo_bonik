package app.common.repo;

import app.common.entity.MadeWith;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface MadeWithRepo extends JpaRepository<MadeWith,Long> {


    boolean existsByNameAndOrgIdAndIdNotIn(String name, Long orgId, List<Long> list);

    boolean existsByNameAndOrgId(String name, Long orgId);

    @Query("select x.id as id , x.name as name , x.orgName as orgName , x.created as created " +
            " from  MadeWith x where ( ?1 is null or x.id=?1 ) and ( ?2 is null or  x.orgId=?2 )  ")
    Page<Map<String, Object>> getList(Long mid, Long orgId, Pageable pageable);

    boolean existsByOrgId(Long id);

    MadeWith findTopByOrgId(Long id);

    boolean existsByOrgIdAndId(Long orgId, Long madeWithId);
}
