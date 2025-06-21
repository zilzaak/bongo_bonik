package app.modules.sales.entity;

import app.common.entity.BaseEntity;;
import jakarta.persistence.Entity;;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
public class InstallmentTnxHistory extends BaseEntity {
    private Long saleId;
    private Double totalDue;
    private Double totalPaid;
    private Double totalBill;
    private String remarks;
}
