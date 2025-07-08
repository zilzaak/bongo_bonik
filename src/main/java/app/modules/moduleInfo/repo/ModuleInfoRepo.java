package app.modules.moduleInfo.repo;

import app.modules.moduleInfo.entity.ModuleInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ModuleInfoRepo extends JpaRepository<ModuleInfo,Long> {

    ModuleInfo findByName(String module);

    boolean existsByName(String name);

    boolean existsByNameAndIdNotIn(String name, List<Long> asList);
}
