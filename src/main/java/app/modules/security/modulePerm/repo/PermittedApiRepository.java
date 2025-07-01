package app.modules.security.modulePerm.repo;

import app.modules.security.modulePerm.entity.PermittedApi;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermittedApiRepository extends JpaRepository<PermittedApi,Long> {

}
