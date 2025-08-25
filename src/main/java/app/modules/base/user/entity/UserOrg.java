package app.modules.base.user.entity;


import app.common.entity.BaseEntity;
import app.modules.base.org.entity.Organization;;
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
@Table(name = "user_org",
        uniqueConstraints = @UniqueConstraint(columnNames = {"org_id", "user_id"}))
public class UserOrg extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "org_id", nullable = false)
    private Organization org;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
