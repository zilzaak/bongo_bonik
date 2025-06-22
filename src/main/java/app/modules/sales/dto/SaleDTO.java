package app.modules.sales.dto;

import app.modules.inventory.entity.Inventory;
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
public class SaleDTO {
    private Long id;
    private String code;
    private Long customer;
    private Double discount;
    private Double vat;
    private Double amount;
    private Double netAmount;
    private Double paid=0.0;
    private Double due=0.0;
    private Integer installment;
    private Long inventory;
    private List<SaleItemDTO> details = new ArrayList<>();
}
