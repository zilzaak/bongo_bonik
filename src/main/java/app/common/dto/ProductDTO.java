package app.common.dto;


import app.common.util.CommonUtil;

public class ProductDTO {

    private Long id;
    private String name;
    private String fullName;
    private Long orgId;
    private Long catId;
    private Long brandId;
    private Long modelId;
    private Long sizeId;
    private Long colorId;
    private Long madeWithId;
    private Long uomId;//KG,LITER,ETC
    private Integer qtyPerUnit;//10,20,30
    private String criteriaIds;

    public PriceDTO getPrice() {
        return price;
    }

    public void setPrice(PriceDTO price) {
        this.price = price;
    }

    private String unitName;
    private Long parentId;
    private String description;
    private PriceDTO price;

    private Boolean confirmSimilarity=Boolean.FALSE;
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getCriteriaIds() {
        return criteriaIds;
    }

    public void setCriteriaIds(String criteriaIds) {
        this.criteriaIds = criteriaIds;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
        this.name = name!=null? CommonUtil.replaceRepeatedChar( name.trim().toUpperCase() ,' '):null; //remove  unnecesarydouble/multiple blank space
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public Long getCatId() {
        return catId;
    }

    public void setCatId(Long catId) {
        this.catId = catId;
    }

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public Long getModelId() {
        return modelId;
    }

    public void setModelId(Long modelId) {
        this.modelId = modelId;
    }

    public Long getSizeId() {
        return sizeId;
    }

    public void setSizeId(Long sizeId) {
        this.sizeId = sizeId;
    }

    public Long getColorId() {
        return colorId;
    }

    public void setColorId(Long colorId) {
        this.colorId = colorId;
    }

    public Long getMadeWithId() {
        return madeWithId;
    }

    public void setMadeWithId(Long madeWithId) {
        this.madeWithId = madeWithId;
    }

    public Long getUomId() {
        return uomId;
    }

    public void setUomId(Long uomId) {
        this.uomId = uomId;
    }

    public Integer getQtyPerUnit() {
        return qtyPerUnit;
    }

    public void setQtyPerUnit(Integer qtyPerUnit) {
        this.qtyPerUnit = qtyPerUnit;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName!=null?unitName.trim().toUpperCase():null;
    }

    public void setQtyUnit(String qtyUnit) {

    }

    public Boolean getConfirmSimilarity() {
        return confirmSimilarity;
    }

    public void setConfirmSimilarity(Boolean confirmSimilarity) {
        this.confirmSimilarity = confirmSimilarity;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

}
