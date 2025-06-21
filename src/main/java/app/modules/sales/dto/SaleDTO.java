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
    private Long customer;
    private Double discount;
    private Double vat;
    private Double amount;
    private Double paid;
    private Double due;
    private Integer installment;
    private Long inventory;
    private List<SaleItemDTO> details = new ArrayList<>();
}
