package app.modules.customer.entity;

import app.common.entity.BaseEntity;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
public class Customer extends BaseEntity {

   private String name;
   private String remarks;
   private String phone;
   private String address;
   private Long orgId;
   private Long branchId;
}
