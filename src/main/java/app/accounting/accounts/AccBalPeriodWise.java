package app.accounting.accounts;

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
public class AccBalPeriodWise extends BaseEntity {
    private String monthYear;//01-2025 means january month 2025
    @ManyToOne
    private Branch branch;
    @ManyToOne
    private ChartOfAccount account;
    private Double periodDebit;
    private Double periodCredit;
    private Double periodBalance;
    private Double totalDebit;
    private Double totalCredit;
    private Double totalBalance;
}
