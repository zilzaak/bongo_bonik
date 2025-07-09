package app.modules.base.urlPerm.entity;

import app.modules.base.role.entity.Role;
import app.modules.base.user.entity.User;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class PermittedModule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long moduleId;

    @ManyToOne
    private User user;

    @ManyToOne
    private Role role;

    @OneToMany(mappedBy = "permittedModule", cascade = CascadeType.ALL,
    orphanRemoval = true , fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<PermittedApi> details = new ArrayList<>();

}
