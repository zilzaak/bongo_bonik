package app.modules.inventory.entity;

import app.common.entity.BaseEntity;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
public class ProdSellPrice  extends BaseEntity {

    private Double unitPrice;

    private Long productId;
    private String productName;

    private Long inventoryId;
    private String inventoryName;

    private LocalDate activeFrom;

    private LocalDate activeTo;

    private Double avgPrice;
    // the product will be under specific org , inventory is ->>under spec branch is -->> under spec org   ----(i)
    // product -->> organization  -----------------------------------------------------------------------------(ii)
    // (i=> org) = (ii=> org)

}
