package app.modules.base.moduleInfo.dto;

import app.common.util.CommonUtil;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MenuDTO {
    public Long id;
    public String frontUrl;
    public String menu;
    public String parentMenu;
    public String apiPattern;
    public String methodName;
    public String apiSeq;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFrontUrl() {
        return frontUrl;
    }

    public void setFrontUrl(String frontUrl) {
        this.frontUrl = CommonUtil.removeAllSpace(frontUrl);
    }

    public String getMenu() {
        return menu;
    }

    public void setMenu(String menu) {
        this.menu = CommonUtil.removeAllSpace(menu);
    }

    public String getParentMenu() {
        return parentMenu;
    }

    public void setParentMenu(String parentMenu) {
        this.parentMenu = CommonUtil.removeAllSpace(parentMenu);
    }

    public String getApiPattern() {
        return apiPattern;
    }

    public void setApiPattern(String apiPattern) {
        this.apiPattern =  CommonUtil.removeAllSpace(apiPattern);
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = CommonUtil.removeAllSpace(methodName);
    }

    public String getApiSeq() {
        return apiSeq;
    }

    public void setApiSeq(String apiSeq) {
        this.apiSeq = apiSeq;
    }
}
