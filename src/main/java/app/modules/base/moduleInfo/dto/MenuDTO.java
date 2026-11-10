package app.modules.base.moduleInfo.dto;

import app.common.util.CommonUtil;

public class MenuDTO {
    public Long id;
    public String frontUrl;
    public String menu;
    public String parentMenu;

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getParentMenu() {
        return parentMenu;
    }

    public void setParentMenu(String parentMenu) {
        this.parentMenu = parentMenu;
    }

    public Long parentId;
    public String apiPattern;
    public String methodName;

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
        if(frontUrl==null || frontUrl.isEmpty()){
            frontUrl=null;
        }
        if(frontUrl!=null && !frontUrl.isEmpty() && CommonUtil.isFirstChar(frontUrl,'/')){
            frontUrl=CommonUtil.removeFirstChar(frontUrl);
        }
        this.frontUrl = frontUrl;
    }

    public String getMenu() {
        return menu;
    }

    public void setMenu(String menu) {
        this.menu = CommonUtil.removeAllSpace(menu);
    }

    public String getApiPattern() {
        return apiPattern;
    }

    public void setApiPattern(String apiPattern) {
        this.apiPattern =  CommonUtil.removeAllSpace(apiPattern);
    }
}
