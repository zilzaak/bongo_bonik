package app.modules.user.security.entity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);

    boolean existsByUsername(String s);
    boolean existsByUsernameAndIdNotIn(String s, List<Long> ids);
}

