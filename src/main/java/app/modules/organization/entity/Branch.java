package app.modules.organization.entity;

import app.common.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
public class Branch extends BaseEntity {
    private String name;
    private String phone;
    private String address;
    private String location;
    @ManyToOne
    private Organization organization;
}
