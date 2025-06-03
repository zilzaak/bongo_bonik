package app.modules.purchase.entity;

import app.common.entity.BaseEntity;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class PurchaseDetails extends BaseEntity{

    private Integer quantity;
    private Long productId;
    private String productType; //barcoded or not barcoded

}
