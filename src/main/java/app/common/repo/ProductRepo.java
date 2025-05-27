package app.common.repo;

import app.common.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepo extends JpaRepository<Product,Long> {

    boolean existsByOrgIdAndFullName(Long orgId, String fullName);

    boolean existsByOrgIdAndFullNameAndIdNotIn(Long orgId, String fullName, List<Long> list);
}
