package app.common.dto;

import app.common.entity.Brand;
import app.common.entity.ProductModel;
import jakarta.persistence.ManyToOne;

public class ProductDTO {

    private String name;
    private Long orgId;
    private Long catId;
    private Long brandId;
    private Long modelId;

    private Long sizeId;
    private Long colorId;
    private Long uomId;//KG,LITER,ETC
    private String uomName;
    private Short qtyPerUnit;//10,20,30
    private String arrangePattern;//BOX_PACKET_SINGLE

    private Long parentId;

}
