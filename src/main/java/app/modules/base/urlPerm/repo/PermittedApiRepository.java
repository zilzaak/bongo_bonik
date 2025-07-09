package app.modules.base.urlPerm.repo;

import app.modules.base.urlPerm.entity.PermittedApi;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermittedApiRepository extends JpaRepository<PermittedApi,Long> {

}
