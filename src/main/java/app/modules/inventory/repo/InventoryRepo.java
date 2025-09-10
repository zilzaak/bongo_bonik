package app.modules.inventory.repo;

import app.modules.base.org.entity.Organization;
import app.modules.inventory.entity.Inventory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface InventoryRepo extends JpaRepository<Inventory,Long> {


    boolean existsByNameAndBranchId(String name, Long branchId);

    boolean existsByNameAndBranchIdAndIdNotIn(String name, Long branchId, List<Long> list);

    boolean existsByBranchId(Long id);

    Inventory findTopByBranchId(Long id);

    @Query("select x from Inventory x where ( ?1 is null or x.org.id=?1 ) and ( ?2 is null or x.branch.id=?2 ) " +
            " and ( ?3 is null or x.id=?3 ) ")
    Page<Object> getList(Long orgId, Long branchId, Long id ,Pageable pageable);

    @Query("select x from Inventory x where  x.org in ?1  and ( ?2 is null or x.branch.id=?2 ) and ( ?3 is null or x.id=?3 ) ")
    Page<Object> getList(List<Organization> orgs, Long branchId, Long id ,Pageable pageable);
}
