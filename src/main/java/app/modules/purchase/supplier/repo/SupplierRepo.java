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
            " from Supplier s left join Organization org on org.id=s.orgId " +
            " where  ( :pk is null or s.id=:pk ) and " +
            "  (:oid is null or org.id=:oid ) and " +
            " ( :commonField is null or  " +
            " cast(s.phone as string)= cast(:commonField as string) or " +
            " upper(cast(s.address as string)) like  concat('%', cast(:commonField as string),'%') or" +
            " upper(cast(s.name as string)) like concat('%',cast(:commonField as string),'%') )")
    Page<Object> getList(@Param("pk") Long pk , @Param("oid") Long oid, @Param("commonField") String commonField, Pageable pageable);

    @Query("select new app.modules.purchase.supplier.dto.SupplierData(s.id,s.name, s.orgId,org.name,s.phone,s.address) " +
            " from Supplier s left join Organization org on org.id=s.orgId " +
            " where  " +
            "  org.id=:oid  ")
    Page<Object> getList(@Param("oid") Long oid, Pageable pageable);


}
