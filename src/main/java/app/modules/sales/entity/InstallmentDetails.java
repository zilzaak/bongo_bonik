package app.modules.sales.entity;

import app.common.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;


@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
public class InstallmentDetails extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    private Sales sales;
    private Double amount;
    private Integer installmentNo;

    @JsonFormat(shape = JsonFormat.Shape.STRING , pattern="yyyy-MM-dd")
    private LocalDate installmentDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING , pattern="yyyy-MM-dd")
    private LocalDate paidDate;

}
