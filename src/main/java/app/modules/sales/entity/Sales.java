package app.modules.sales.entity;


import app.common.entity.BaseEntity;
import app.modules.sales.customer.entity.Customer;
import app.modules.inventory.entity.Inventory;
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

    private String code;
    @ManyToOne(fetch = FetchType.LAZY)
    private Customer customer;

    private Double discount;
    private Double vat;
    private Double amount;
    private Double netAmount;
    private Double paid;
    private Double due;
    private Integer installment;

    @ManyToOne(fetch = FetchType.LAZY)
    private Inventory inventory;
    private String paymentMethods;
    private String saleType;
    private Double paidPercentage;

    @OneToMany(mappedBy = "sales", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<SalesItems> details = new ArrayList<>();

}
