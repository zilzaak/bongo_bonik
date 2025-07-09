package app.modules.sales.customer.repo;

import app.modules.sales.customer.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerRepo extends JpaRepository<Customer,Long> {
    boolean existsByOrgIdAndPhone(Long orgId, String phone);
    boolean existsByOrgIdAndPhoneAndIdNotIn(Long orgId, String phone, List<Long> ids);
}
