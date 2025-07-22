package app.accounting.ledger;

import app.accounting.accounts.ChartOfAccount;
import app.common.entity.BaseEntity;
import app.common.entity.Branch;
import app.common.entity.Organization;
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
    private Organization org;
    @ManyToOne
    private Branch branch;
    @ManyToOne
    private ChartOfAccount account;
    @ManyToOne
    private ChartOfAccount against;
    private Long refId;
    private String refCode;
    private String particulars;
    private Double debit;
    private Double credit;

}
