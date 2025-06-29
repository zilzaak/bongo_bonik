package app.modules.user.security.repo;



import app.modules.user.security.entity.AuthorityPermission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuthorityPermissionRepository extends JpaRepository<AuthorityPermission, Long> {
    List<AuthorityPermission> findByRoleName(String roleName);

    boolean existsByRoleNameAndApiPattern(String roleName, String apiPattern);
    boolean existsByRoleNameAndApiPatternAndIdNotIn(String roleName, String apiPattern,List<Long> ids);
}
