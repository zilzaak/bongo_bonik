package app.modules.security.modulePerm.entity;

import app.modules.security.entity.Role;
import app.modules.security.entity.User;
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

    @OneToMany(mappedBy = "permittedModule", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<PermittedApi> details = new ArrayList<>();

}
