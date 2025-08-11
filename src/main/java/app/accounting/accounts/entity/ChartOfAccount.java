package app.accounting.accounts.entity;

import app.common.entity.BaseEntity;
import app.common.entity.Branch;
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
public class ChartOfAccount extends BaseEntity {
    @ManyToOne
    private Branch branch;

    private String accName;

    @ManyToOne
    private ChartOfAccount parent;
    private Boolean itIsRootAcc;
    private String accountType; //income,expense,asset,liabilities,equity(withdrawal,owner_capital,retained_earnings)
    private String accTypeHierarchy; // EXPENSE->UTILITY_EXPENSE->HOUSE_RENT

}
