package app.modules.base.user.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class UserDTO {

    private Long id;
    private String username;
    private String password;
    private String phone;
    private String email;
    private String address;
    private String displayName;
    private Boolean enabled;
    private List<String> roles;
    private List<UserOrgDTO> userOrgs=new ArrayList<>();

}
