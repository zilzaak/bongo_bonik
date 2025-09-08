package app.common.entity;

import app.common.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
public class CostPrice extends BaseEntity {

    private Double price; //org price
    @ManyToOne
    @JoinColumn(unique = true)
    private Product product;
    private Double avgPrice;//avg org price

    // the product will be under specific org , inventory is ->>under spec branch is -->> under spec org   ----(i)
    // product -->> organization  -----------------------------------------------------------------------------(ii)
    // (i=> org) = (ii=> org)
}
