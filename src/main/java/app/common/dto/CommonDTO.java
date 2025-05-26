package app.common.dto;


import lombok.Data;


public class CommonDTO {

    private Long id;
    private String name;
    private Long orgId;
    private String orgName;

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

    public String getOrgName() {
        return orgName;
    }

    public String getEntity() {
        return entity;
    }

    private String entity;

    public void setName(String name) {
        this.name = name!=null?name.trim().toUpperCase():null;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName!=null?orgName.trim().toUpperCase():null;
    }
}
