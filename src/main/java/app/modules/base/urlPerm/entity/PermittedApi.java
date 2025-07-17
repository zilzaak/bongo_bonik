package app.modules.base.urlPerm.entity;

import app.common.entity.BaseEntity;
import app.modules.base.role.entity.Role;
import app.modules.base.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "api_permission")
public class PermittedApi extends BaseEntity{
    private String backendUrl;
    private String frontendUrl;
    @ManyToOne(fetch = FetchType.EAGER)
    private User user;
    @ManyToOne(fetch = FetchType.EAGER)
    private Role role;
    private Long menuId;
    private String menuIdsHierarchy;
}
