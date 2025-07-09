package app.modules.purchase.supplier.dto;

import app.common.util.CommonUtil;

public class SupplierDTO {
    private Long id;
    private String name;
    private String phone;
    private String address;
    private Long orgId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = CommonUtil.replaceRepeatedChar(name,' ');
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = CommonUtil.removeAllSpace(phone);
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = CommonUtil.replaceRepeatedChar(address,' ');
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }
}
