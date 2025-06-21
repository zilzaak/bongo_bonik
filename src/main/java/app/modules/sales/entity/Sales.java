package app.modules.sales.entity;


import app.common.entity.BaseEntity;
import app.modules.customer.entity.Customer;
import app.modules.inventory.entity.Inventory;
import app.modules.purchase.entity.PurchaseDetails;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
public class Sales extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    private Customer customer;

    private Double discount;
    private Double vat;
    private Double amount;
    private Double paid;
    private Double due;
    private Integer installment;

    @ManyToOne(fetch = FetchType.LAZY)
    private Inventory inventory;

    @OneToMany(mappedBy = "sales", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<SalesItems> details = new ArrayList<>();

}
