package app.common.repo;

import app.common.entity.Branch;
import app.modules.base.org.entity.Organization;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

public interface BranchRepo extends JpaRepository<Branch,Long> {

//    boolean existsByNameAndOrg(String name, Organization org);
//
//    boolean existsByNameAndOrgAndIdNotIn(String name, Organization org , List<Long> list);


    @Query("select count(x) from Branch x where " +
            " ( upper(x.name) like concat('%', upper(?1),'%')  or upper(x.name) like concat('%',upper(?1),'%') ) and x.org.id=?2  ")
    int checkExistName(String name , Long orgId);

    @Query("select count(x) from Branch x where " +
            " ( upper(x.name) like concat('%', upper(:nm),'%')  or upper(x.name) like concat('%',upper(:nm),'%') )  " +
            " and x.org.id=:orgid and x.id not in :list  ")
    int checkExistNameEdit(@Param("nm") String nm,@Param("orgid") Long orgid ,  @Param("list") List<Long> list);



    @Query("select b.id as id , b.name as name , " +
            " org.id as orgId , " +
            "  b.phone as phone , " +
            "  b.address as address , " +
            "  b.location as  location ," +
            "  b.created as created ," +
            "   org.name as orgName , " +
            "  b.createBy as createBy , " +
            "  b.updateBy as updateBy , " +
            "  b.updated as updated " +
            " from Branch b join b.org org " +
            " where ( ?1 is null or b.id=?1 ) and ( ?2 is null or org.id=?2 ) ")
    Page<Map<String, Object>> getList(Long branchId, Long orgId, Pageable pageable);

    boolean existsByOrgId(Long id);

    Branch findTopByOrgId(Long id);

    boolean existsByIdAndOrgId(Long id, Long orgId);

    interface OrgRepo extends JpaRepository<Organization,Long> {


        @Query("select x.name from Organization x where x.id=:id")
        String getName(@Param("id") Long id);

        @Query("select count(x) from Organization x where " +
                " ( upper(x.name) like concat('%', upper(?1),'%')  or upper(x.name) like concat('%',upper(?1),'%') )  ")
        int checkExistName(String name);

        @Query("select count(x) from Organization x where " +
                " ( upper(x.name) like concat('%', upper(:nm),'%')  or upper(x.name) like concat('%',upper(:nm),'%') ) and x.id not in :list  ")
        int checkExistNameEdit(@Param("nm") String nm, @Param("list") List<Long> list);

        @Query("select b.id as id , b.name as orgName , b.phone as orgPhone , " +
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
}
