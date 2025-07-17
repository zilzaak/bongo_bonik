package app.modules.base.urlPerm.dto;
import app.common.util.CommonUtil;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PrmttedApiDTO {
    private Long id;
    private String backendUrl;
    private String frontendUrl;
    private Long user;
    private Long role;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBackendUrl() {
        return backendUrl;
    }

    public void setBackendUrl(String backendUrl) {
        this.backendUrl = CommonUtil.removeAllSpace(backendUrl);
        if(CommonUtil.isLastChar(this.backendUrl,'/')){
            this.backendUrl=CommonUtil.removeLastCharacter(this.backendUrl);
        }
    }

    public String getFrontendUrl() {
        return frontendUrl;
    }

    public void setFrontendUrl(String frontendUrl) {
        this.frontendUrl = frontendUrl;
    }

    public Long getUser() {
        return user;
    }

    public void setUser(Long user) {
        this.user = user;
    }

    public Long getRole() {
        return role;
    }

    public void setRole(Long role) {
        this.role = role;
    }

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }

    public String getMenuIdsHierarchy() {
        return menuIdsHierarchy;
    }

    public void setMenuIdsHierarchy(String menuIdsHierarchy) {
        this.menuIdsHierarchy = menuIdsHierarchy;
    }

    private Long menuId;
    private String menuIdsHierarchy;
}
