package app.modules.base.user.repo;

import app.modules.base.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);

    boolean existsByUsername(String s);
    boolean existsByUsernameAndIdNotIn(String s, List<Long> ids);
}

