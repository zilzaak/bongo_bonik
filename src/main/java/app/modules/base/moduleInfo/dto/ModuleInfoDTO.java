package app.modules.base.moduleInfo.dto;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class ModuleInfoDTO {
    private Long id;
    private String name;
    private List<AgainstModuleDTO> details = new ArrayList<>();
}
