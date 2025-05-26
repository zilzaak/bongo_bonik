package app.modules.inventory.entity;

import app.common.entity.BaseEntity;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
public class StockBalance extends BaseEntity {

    private String name;
    private Long inventoryId;

    private Long productId;
    private String productName;

    private Double quantity;
    private Double avgPrice;

}
