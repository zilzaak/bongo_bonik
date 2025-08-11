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
public class PeriodWiseAccBal extends BaseEntity {
    private String monthYear;//01-2025 means january month 2025
    @ManyToOne
    private Branch branch;
    @ManyToOne
    private ChartOfAccount account;
    private Double periodDebit; // for example how amount debited in month of february 2025
    private Double periodCredit;// for example how amount credited in month of february 2025
    private Double periodBalance;// for example what is the revenue earned in month of february 2025
    private Double totalDebit;//total debit from start till now in the business / updated debit
    private Double totalCredit;
    private Double totalBalance;
}
