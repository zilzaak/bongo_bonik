package app.modules.moduleInfo.repo;

import app.modules.moduleInfo.entity.ApiAgainstModule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApiAgainstModuleRepo extends JpaRepository<ApiAgainstModule,Long> {

}
