package app.modules.supplier.entity;

import app.common.entity.BaseEntity;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Supplier extends BaseEntity{

    private String name;
    private String phone;
    private String address;
    private Long orgId;

}
