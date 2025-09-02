package app.common.entity;

import app.modules.base.org.entity.Organization;
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
    @ManyToOne
    private Organization org;
    @ManyToOne
    private ProductCat cat;
    @ManyToOne
    private Brand brand;
    @ManyToOne
    private ProductModel model;
    @ManyToOne
    private ProductSize size;
    @ManyToOne
    private ProductColor color;
    @ManyToOne
    private MadeWith madeWith;
    @ManyToOne
    private UnitOfMeasure uom;
    private Integer qtyPerUnit; // amount per unit(ml,liter,kg , gm ,kilo,km ,meter etc)
    private String unitName;  // for example ml , liter , kg , gm , kilo , km  , meter etc
    private Long parentId;
    private String description;
    private String criteriaIds; //124,247,584
    //full name = brand+model+color+size+madeWith+qtyPerUnit+qtyUnit+uomId
}
