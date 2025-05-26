package app.common.repo;

import app.common.entity.ProductModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductModelRepo extends JpaRepository<ProductModel,Long> {

    boolean existsByNameAndBrandId(String name, Long brandId);

    boolean existsByNameAndBrandIdAndIdNotIn(String name, Long brandId, List<Long> list);
}