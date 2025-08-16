package app.common.entity;

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
public class SellPrice extends BaseEntity {

    private Double unitPrice;
    private Long productId;
    private String productName;
    private Long orgId;
    private String orgName;
    private Double avgPrice;
    // the product will be under specific org , inventory is ->>under spec branch is -->> under spec org   ----(i)
    // product -->> organization  -----------------------------------------------------------------------------(ii)
    // (i=> org) = (ii=> org)
}
