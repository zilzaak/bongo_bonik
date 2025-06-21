package app.modules.sales.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class SaleItemDTO {
    private Long id;
    private Long product;
    private String productType; //barcoded or not barcoded
    private Double unitPrice;
    private Integer quantity;
    private Double amount;
    private Double discPct;
    private Double discAmount;
    private Double vatPct;
    private Double vatAmount;
}
