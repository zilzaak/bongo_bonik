package app.common.entity;

import app.modules.organization.entity.Organization;
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

    @ManyToOne
    private Organization org;

    private String name;
    private String fullName;
    private String code;

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

    private Short quantityPerUnit;//10,20,30

    @ManyToOne
    private UnitOfMeasure uom;//KG,LITER,ETC

    private String arrangePattern;//BOX_PACKET_SINGLE

    private Long parentId;

//   name = name+brand+model+color+size+quantityPerUnit+uom

}
