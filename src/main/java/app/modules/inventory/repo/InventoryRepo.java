package app.modules.inventory.repo;

import app.modules.inventory.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InventoryRepo extends JpaRepository<Inventory,Long> {


    boolean existsByNameAndBranchId(String name, Long branchId);

    boolean existsByNameAndBranchIdAndIdNotIn(String name, Long branchId, List<Long> list);
}
