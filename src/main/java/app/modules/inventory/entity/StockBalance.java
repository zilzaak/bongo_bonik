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

    private Long  productId;
    private Long orgId;
    private Long branchId;
    private Long inventoryId;

    private String productName;
    private String orgName;
    private String branchName;
    private String inventoryName;

    private Integer quantity;
    private Double unitPrice;

}
