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

    private String title;

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

    private Short quantityPerUnit; // in case of medicine  or baby toy or cosmetics or

    @ManyToOne
    private UnitOfMeasure uom;

    private String productPattern;

    private Long parentId;

}
