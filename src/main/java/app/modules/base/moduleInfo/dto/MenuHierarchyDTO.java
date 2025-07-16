package app.modules.base.moduleInfo.dto;


import app.modules.base.moduleInfo.entity.MenuHierarchy;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
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
