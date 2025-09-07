package app.modules.inventory.dto;

import lombok.Data;

@Data
public class PricingDTO {

    private Long id;
    private Double price;
    private Long productId;
    private Long orgId;
    private String entity;

}
