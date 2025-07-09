package app.modules.base.role.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="acl_role")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String authority;

}
