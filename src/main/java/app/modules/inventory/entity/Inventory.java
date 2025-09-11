package app.modules.inventory.entity;

import app.common.entity.BaseEntity;
import app.common.entity.Branch;
import app.modules.base.org.entity.Organization;
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
@Table(name = "inventory", uniqueConstraints = {
@UniqueConstraint(columnNames = {"name", "branch_id"}
                )})
public class Inventory  extends BaseEntity{

    @Column(nullable = false,name = "name")
    private String name;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false,name = "branch_id")
    private Branch branch;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Organization org;
    private String phone;
    private String others;
}
