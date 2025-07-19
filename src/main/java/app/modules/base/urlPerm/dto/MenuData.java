package app.modules.base.urlPerm.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class MenuData {
    private String frontUrl;  //frontendUrl
    private String menu;
    private String parentMenu;
    private String apiPattern; //backendUrl
    private String methodName; //put , post , delete , update , patch
    private String apiSeq;

    public MenuData(String frontUrl,
                    String menu,
                    String parentMenu,
                    String apiPattern,
                    String methodName,
                    String apiSeq) {
        this.frontUrl = frontUrl;
        this.menu = menu;
        this.parentMenu = parentMenu;
        this.apiPattern = apiPattern;
        this.methodName = methodName;
        this.apiSeq = apiSeq;
    }
}
