package app.modules.base.moduleInfo.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MenuHierarchyDTO {
    public Long id;
    public String frontUrl;  //frontendUrl
    public String menu;
    public String parentMenu;
    public String apiPattern; //backendUrl
    public String methodName; //put , post , delete , update , patch
    public String apiSeq;
    private List<MenuHierarchyDTO> details = new ArrayList<>();
}
