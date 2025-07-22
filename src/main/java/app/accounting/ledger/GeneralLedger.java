package app.accounting.ledger;

import app.accounting.accounts.ChartOfAccount;
import app.common.entity.BaseEntity;
import app.common.entity.Branch;
import app.common.entity.Organization;
import app.modules.purchase.supplier.entity.Supplier;
import app.modules.sales.customer.entity.Customer;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GeneralLedger extends BaseEntity {
    @ManyToOne
    private Branch branch;
    @ManyToOne
    private ChartOfAccount account;
    @ManyToOne
    private ChartOfAccount against;
    private Double debit;
    private Double credit;

    @ManyToOne
    private Supplier supplier;
    @ManyToOne
    private Customer customer;
    private Long refId;
    private String refCode;
    private String particulars;
    private String remarks;
}
