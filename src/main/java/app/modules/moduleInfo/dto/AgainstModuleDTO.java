package app.modules.moduleInfo.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AgainstModuleDTO {
    private Long id;
    private String apiPattern; //backendUrl
    private String methodName; //put , post , delete , update , patch
    private String frontUrl;  //frontendUrl
}
