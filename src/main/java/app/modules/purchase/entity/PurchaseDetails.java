package app.modules.purchase.entity;

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

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class PurchaseDetails extends BaseEntity{
    private Integer quantity;
    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;
    private String productType;
    private Double unitPrice;
    private Double disAmount;
    private Double vatAmount;
    private Double totalAmount;// totalAmount=total price-discount+vatAmount
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    @JsonBackReference
    private Purchase purchase;
}
