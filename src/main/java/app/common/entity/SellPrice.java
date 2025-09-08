package app.common.entity;

import app.common.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.UniqueConstraint;
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
    @ManyToOne
    @JoinColumn(unique = true)
    private Product product;
    private String sellBranchIds;
    private String sellPrices;
    private Double defaultSellPrice;
    private String costBranchIds;
    private String costPrices;
    private Float  defaultCostPrice;
}
