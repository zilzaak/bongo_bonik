package app.modules.inventory.entity;

import app.common.entity.BaseEntity;
import app.modules.organization.entity.Branch;
import app.modules.organization.entity.Organization;
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
public class Inventory  extends BaseEntity{

    private String name;
    private String phone;
    private String address;

    private Long orgId;
    private String orgName;

    private Long branchId;
    private String branchName;
}
