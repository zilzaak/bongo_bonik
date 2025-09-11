package app.modules.purchase.dto;

import app.common.util.CommonUtil;
import lombok.Data;

@Data
public class PurchaseDetailsDTO {
    private Long id;
    private Integer quantity;
    private Long productId;
    private String productType;//barcoded or not barcoded
    private Double unitPrice;
    private Double amount;
    private Long purchaseId;
    //for view purpose added beloww two field
    private Long productName;
    private String productCode;
    public void setProductType(String productType) {
        this.productType = CommonUtil.removeHeadTailSpace(productType);
    }
}
