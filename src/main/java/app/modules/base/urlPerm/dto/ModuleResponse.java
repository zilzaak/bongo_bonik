package app.modules.base.urlPerm.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Setter
@Getter
public class ModuleResponse {
    private String title;
    private List<String> roles= Arrays.asList("SUPPER_ADMIN");
    boolean collapsed=false;
    private List<ModuleMenu> submenus=new ArrayList<>();

}
