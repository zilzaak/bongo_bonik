package app.modules.purchase.entity;

import app.common.entity.BaseEntity;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Purchase  extends BaseEntity {

  private Long orgId;
  private Long branchId;
  private Long inventoryId;
  private Long supplierId;

  private String orgName;
  private String branchName;
  private String inventoryName;

  private Double totalBill;
  private Double dueAmount;
  private String code;
}
