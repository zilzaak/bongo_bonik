package app.common.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
public class Pricing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(unique = true,nullable = false)
    private Product product;
    private String sellBranchIds;
    private String sellPrices;
    private Double defaultSellPrice;
    private String costBranchIds;
    private String costPrices;
    private Double  defaultCostPrice;
}
