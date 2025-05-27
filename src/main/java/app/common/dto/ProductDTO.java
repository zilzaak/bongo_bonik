package app.common.dto;


import lombok.Data;

@Data
public class ProductDTO {

    private String name;
    private Long orgId;
    private Long catId;
    private Long brandId;
    private Long modelId;
    private Long sizeId;
    private Long colorId;
    private Long madeWithId;
    private Long uomId;//KG,LITER,ETC
    private Integer qtyPerUnit;//10,20,30
    private String qtyUnit;
    private Long parentId;

}
