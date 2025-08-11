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
public class AccountBalance extends BaseEntity {
    @ManyToOne
    private Branch branch;
    @ManyToOne
    private ChartOfAccount account;
    private Double balance;
    private Double totDebit;
    private Double totCredit;
}
