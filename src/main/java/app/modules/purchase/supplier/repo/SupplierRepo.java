package app.modules.purchase.supplier.repo;

import app.modules.purchase.supplier.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SupplierRepo extends JpaRepository<Supplier,Long> {
    boolean existsByPhoneAndOrgId(String phone, Long orgId);

    boolean existsByPhoneAndOrgIdAndIdNotIn(String phone, Long orgId, List<Long> asList);
}
