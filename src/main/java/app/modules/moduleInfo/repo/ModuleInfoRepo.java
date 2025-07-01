package app.modules.moduleInfo.repo;

import app.modules.moduleInfo.entity.ModuleInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ModuleInfoRepo extends JpaRepository<ModuleInfo,Long> {


    ModuleInfo findByName(String module);
}
