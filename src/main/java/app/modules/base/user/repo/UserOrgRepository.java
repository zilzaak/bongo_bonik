package app.modules.base.user.repo;

import app.modules.base.user.entity.User;
import app.modules.base.user.entity.UserOrg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserOrgRepository extends JpaRepository<UserOrg, Long> {
    List<UserOrg> findByUser(User user);
}
