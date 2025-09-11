package app.modules.purchase.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PurchaseDTO {
    private String code;
    private Long id;
    private Long inventoryId;
    private Long supplierId;
    private Double totalBill;
    private Double dueAmount;
    private List<PurchaseDetailsDTO> dtls=new ArrayList<>();
}
