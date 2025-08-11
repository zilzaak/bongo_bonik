package app.modules.base.role.repo;

import app.modules.base.role.entity.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Long> {

    boolean existsByAuthority(String authority);

    boolean existsByAuthorityAndIdNotIn(String authority, List<Long> asList);

    Role findByAuthority(String authority);

    @Query("SELECT r from Role r ")
    Page<Object> list(Pageable pageable);
}
