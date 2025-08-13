package app.modules.base.user.entity;

import app.modules.base.role.entity.Role;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;


import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="acl_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String password;
    @NotBlank(message = "Username is required")
    @Column(unique = true, nullable = false)
    private String username;
    @NotBlank(message = "Phone is required")
    @Column(unique = true, nullable = false)
    private String phone;
    private String email;
    @NotBlank(message = "Address is required")
    @Column(nullable = false)
    private String address;
    @NotBlank(message = "Full name is required")
    @Column(nullable = false)
    private String displayName;
    private Boolean enabled;

    @CreationTimestamp
    @JsonFormat(shape = JsonFormat.Shape.STRING ,
            pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime created;

    @UpdateTimestamp
    @JsonFormat(shape = JsonFormat.Shape.STRING ,
            pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updated;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "acl_user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    @Transient
    private String roleName;

}