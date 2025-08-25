package app.modules.base.user.repo;

import app.modules.base.user.entity.User;
import app.modules.base.user.entity.UserOrg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface UserOrgRepository extends JpaRepository<UserOrg, Long> {
    List<UserOrg> findByUser(User user);

    @Query("select x.id as id ," +
            "  o.id as orgId , o.name as orgName ," +
            "  u.id as userId , u.username as username " +
            " from UserOrg x " +
            " left join x.org o " +
            " left join x.user u " +
            "  where u.id=?1 ")
    List<Map<String,Object>> getList(Long id);

    @Query("select  o.name from UserOrg x " +
            "  join x.org o " +
            "  join x.user u " +
            "  where u.id=?1 ")
    List<Object[]> orgnames(Long id);
}
