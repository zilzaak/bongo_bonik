package app.modules.purchase.supplier.repo;

import app.modules.purchase.supplier.dto.SupplierData;
import app.modules.purchase.supplier.entity.Supplier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SupplierRepo extends JpaRepository<Supplier,Long> {
    boolean existsByPhoneAndOrgId(String phone, Long orgId);

    boolean existsByPhoneAndOrgIdAndIdNotIn(String phone, Long orgId, List<Long> asList);
    @Query("select new app.modules.purchase.supplier.dto.SupplierData(s.id,s.name, s.orgId,org.name,s.phone,s.address) " +
            " from Supplier s join Organization org on org.id=s.orgId " +
            " where ( :id is null or s.id=:id) and " +
            "  (:orgId is null or s.orgId=:orgId ) and ( :commonField is null or  " +
            " cast(s.phone as string)= cast(:commonField as string) or " +
            " upper(cast(s.address as string)) like  concat('%', cast(:commonField as string),'%') or" +
            " upper(cast(s.name as string)) like concat('%',cast(:commonField as string),'%') )")
    Page<Object> getList(@Param("id") Long id , @Param("orgId") Long orgId, @Param("commonField") String commonField, Pageable pageable);
}
