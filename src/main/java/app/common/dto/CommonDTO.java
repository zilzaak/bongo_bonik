package app.common.dto;


import app.common.util.CommonUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
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



    public void setProductCatIds(String productCatIds) {
        this.productCatIds = productCatIds!=null?productCatIds.trim().replaceAll("\\s+", ""):null;
    }

    public void setPhone(String phone) {
        this.phone = phone!=null?phone.trim().toUpperCase():null;;
    }


    public void setEntity(String entity) {

        this.entity = entity!=null?entity.trim().toUpperCase():null;

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
