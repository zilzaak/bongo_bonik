package app.common.dto;


import app.common.util.CommonUtil;

public class CommonDTO {

    private Long id;
    private String name;
    private Long orgId;
    private Long brandId;
    private String orgName;
    private String brandName;
    private String productCatIds;
    private String phone;
    private String entity;
    private String address;
    private String location;
    private String remarks;

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getProductCatIds() {
        return productCatIds;
    }

    public void setProductCatIds(String productCatIds) {
        this.productCatIds = productCatIds!=null?productCatIds.trim().replaceAll("\\s+", ""):null;
    }

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone!=null?phone.trim().toUpperCase():null;;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public void setEntity(String entity) {
        this.entity = entity!=null?entity.trim().toUpperCase():null;
    }

    public String getName() {
        return name;
    }

    public Long getOrgId() {
        return orgId;
    }

    public String getBrandName() {
        return brandName;
    }

    public String getOrgName() {
        return orgName;
    }

    public String getEntity() {
        return entity;
    }

    public void setName(String name) {
        if(this.entity!=null){
            if(this.entity.equalsIgnoreCase("UnitOfMeasure")){
                this.name = name!=null? CommonUtil.removeAllSpace(name.trim().toLowerCase()):null;
            }else{
                this.name = name!=null?CommonUtil.removeAllSpace(name.trim().toUpperCase()):null;
            }
        }else{
            this.name = name;
        }

    }
    public void setOrgName(String orgName) {
        this.orgName = orgName!=null?CommonUtil.replaceRepeatedChar(orgName.trim().toUpperCase(),' '):null;
    }
    public void setBrandName(String brandName) {
        this.brandName = brandName!=null?CommonUtil.replaceRepeatedChar(brandName.trim().toUpperCase(),' '):null;
    }
}
