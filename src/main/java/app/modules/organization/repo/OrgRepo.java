package app.modules.organization.repo;

import app.modules.organization.entity.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrgRepo extends JpaRepository<Organization,Long> {


    @Query("select x.name from Organization x where x.id=:id")
    String getName(@Param("id") Long id);

    boolean existsByName(String name);

    boolean existsByNameAndIdNotIn(String name, List<Long> list);
}
