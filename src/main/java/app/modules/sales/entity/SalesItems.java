package app.modules.sales.entity;

import app.common.entity.BaseEntity;
import app.common.entity.Product;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
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
    private Double netAmount;// the total price after discount
    private Double grandAmount;//final amount after adding vat
    private Double discPct;
    private Double discAmount;
    private Double vatPct;
    private Double vatAmount;
    private String code;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    @JsonBackReference
    private Sales sales;
    @Transient
    private Integer totalQuantity;

}
