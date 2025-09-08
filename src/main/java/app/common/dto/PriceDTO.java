package app.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class PriceDTO {
  private Long id;
  private String sellBranchIds;
  private String sellPrices;
  private Double defaultSellPrice;
  private String costBranchIds;
  private String costPrices;
  private Float  defaultCostPrice;
}
