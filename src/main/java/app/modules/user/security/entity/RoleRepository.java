package app.modules.user.security.entity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Long> {

    boolean existsByAuthority(String authority);

    boolean existsByAuthorityAndIdNotIn(String authority, List<Long> asList);

    Role findByAuthority(String authority);
}
