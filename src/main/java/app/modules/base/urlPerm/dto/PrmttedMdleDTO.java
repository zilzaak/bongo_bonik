package app.modules.base.urlPerm.dto;

import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class PrmttedMdleDTO {
    private Long id;
    private Long moduleId;
    private Long user;
    private Long role;
    private List<PrmttedApiDTO> details=new ArrayList<>();
}
