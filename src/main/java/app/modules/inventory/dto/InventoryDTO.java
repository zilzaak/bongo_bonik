package app.modules.inventory.dto;

import app.common.util.CommonUtil;

public class InventoryDTO {

    private Long id;
    private String name;
    private String phone;
    private String others;
    public String getOthers(){
        return others;
    }

    public void setOthers(String others) {
        this.others = others;
    }

    private Long branchId;
    private String branchName;

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    private Long orgId;
    private String orgName;

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }


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

        this.name = name!=null?name.trim().toUpperCase():null;
        this.name= CommonUtil.replaceRepeatedChar(this.name,' ');

    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Long getBranchId() {
        return branchId;
    }

    public void setBranchId(Long branchId) {
        this.branchId = branchId;
    }
}
