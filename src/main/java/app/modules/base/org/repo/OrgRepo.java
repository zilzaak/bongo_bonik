package app.modules.base.org.repo;


import app.modules.base.org.entity.Organization;
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

    @Query("select count(x) from Organization x where " +
            " ( upper(x.name) like concat('%', upper(?1),'%')  or upper(x.name) like concat('%',upper(?1),'%') )  ")
    int checkExistName(String name);

    @Query("select count(x) from Organization x where " +
            " ( upper(x.name) like concat('%', upper(:nm),'%')  or upper(x.name) like concat('%',upper(:nm),'%') ) and x.id not in :list  ")
    int checkExistNameEdit(@Param("nm") String nm, @Param("list") List<Long> list);

    @Query("select b  "+
            " from Organization b   " +
            " where ( :oid is null or b.id=:oid ) and " +
            "  ( cast(:cf as String)  is null or upper(cast(b.name as String) ) like concat('%', upper(cast(:cf as String)),'%') or " +
            "  upper(cast(b.remarks as String)) like concat('%', upper(cast(:cf as String)),'%') or " +
            "  upper(cast(b.address as String))  like concat('%', upper(cast(:cf as String)),'%') or " +
            "  cast(b.phone as String) like concat('%', cast(:cf as String) ,'%') ) ")
    Page<Object> getList(@Param("oid") Long oid,
                                      @Param("cf") String cf,
                                      Pageable pageable);
}
