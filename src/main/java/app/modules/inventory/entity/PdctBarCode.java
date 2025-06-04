package app.modules.inventory.entity;

import app.common.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


@Entity
@NoArgsConstructor
@AllArgsConstructor
public class PdctBarCode extends BaseEntity {
    @ManyToOne
    private StockBalance stockBal;
    private String barCode;

}
