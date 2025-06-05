package app.modules.inventory.repo;

import app.modules.inventory.entity.PdctBarCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BarCodeRepo extends JpaRepository<PdctBarCode,Long> {
}
