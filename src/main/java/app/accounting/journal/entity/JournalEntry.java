package app.accounting.journal.entity;

import app.accounting.accounts.entity.ChartOfAccount;
import app.common.entity.BaseEntity;
import app.common.entity.Branch;
import app.modules.purchase.supplier.entity.Supplier;
import app.modules.sales.customer.entity.Customer;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class JournalEntry extends BaseEntity {
    @ManyToOne
    private Branch branch;
    @ManyToOne
    private ChartOfAccount account;
    @ManyToOne
    private ChartOfAccount against;
    private Double debit;
    private Double credit;
    @JsonFormat(shape = JsonFormat.Shape.STRING ,
            pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime postDate;

    @ManyToOne
    private Supplier supplier;
    @ManyToOne
    private Customer customer;
    private Long refId;  // sales invoice id , or purchase id ,
    private String refCode; //invoice,code etc
    private String particulars;
    private String invoiceType;
}
