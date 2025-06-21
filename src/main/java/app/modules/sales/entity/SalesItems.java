package app.modules.sales.entity;

import app.common.entity.BaseEntity;
import app.common.entity.Product;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
public class SalesItems extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;
    private String productType; //barcoded or not barcoded
    private Double unitPrice;
    private Integer quantity;
    private Double amount;
    private Double discPct;
    private Double discAmount;
    private Double vatPct;
    private Double vatAmount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    @JsonBackReference
    private Sales sales;

}
