package app.modules.purchase.dto;

import app.common.util.CommonUtil;
import lombok.Data;

@Data
public class PurchaseDetailsDTO {
    private Long id;
    private Integer quantity;
    private Long productId;
    private String productType;
    private Double unitPrice;
    private Long purchaseId;
    private Long productName;
    private String productCode;
    private Double disAmount;
    private Double vatAmount;
    private Double totalAmount;// totalAmount=total price-discount+vat
    public void setProductType(String productType) {
        this.productType = CommonUtil.removeHeadTailSpace(productType);
    }
}
