package app.modules.base.user.repo;

import app.modules.base.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);

    boolean existsByUsername(String s);
    boolean existsByUsernameAndIdNotIn(String s, List<Long> ids);

    @Query("SELECT x.created as created ," +
            " x.updated as updated , " +
            "x.username as username , " +
            "x.id as id ," +
            " x.enabled as enabled ," +
            "x.email as email, " +
            "x.phone as phone , " +
            "x.address as address " +
            " from User x where ( :ur is null or x.username=:ur )  ")
    Page<Map<String, Object>> list(@Param("ur") String ur, Pageable pageable);

    boolean existsByPhone(String phone);

    boolean existsByPhoneAndIdNotIn(String phone, List<Long> asList);
}

