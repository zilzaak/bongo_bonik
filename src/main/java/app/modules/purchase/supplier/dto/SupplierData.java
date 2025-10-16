package app.modules.purchase.supplier.dto;

import lombok.Data;

@Data
public class SupplierData {

    private Long id;
    private String name;
    private Long orgId;
    private String orgName;
    private String phone;
    private String address;

    public SupplierData(Long id, String name ,Long orgId , String orgName, String phone, String address) {
        this.id = id;
        this.name=name;
        this.orgId = orgId;
        this.orgName = orgName;
        this.phone = phone;
        this.address = address;
    }
}
