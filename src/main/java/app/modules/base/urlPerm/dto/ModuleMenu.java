package app.modules.base.urlPerm.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ModuleMenu {
    private String title;
    private String link;
    private String method;
    private String backendUrl;
}
