package app.modules.inventory.entity;

import app.common.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class PdctBarCode extends BaseEntity {
    @ManyToOne
    private StockBalance stockBal;
    private String barCode;

}
