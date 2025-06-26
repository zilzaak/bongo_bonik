package app.common.entity;

import jakarta.persistence.Entity;
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
public class Product extends BaseEntity{

    private String name;
    private String fullName;
    private String code;

    private Long orgId;
    private Long catId;
    @ManyToOne
    private Brand brand;
    @ManyToOne
    private ProductModel model;
    private Long sizeId;
    private Long colorId;
    private Long madeWithId;
    private Long uomId;
    private Integer qtyPerUnit; // amount per unit
    private String unitName;  // for example ml , liter , kg , gm , kilo , km  , meter etc
    private Long parentId;
    //full name = brand+model+color+size+madeWith+qtyPerUnit+qtyUnit+uomId
}
