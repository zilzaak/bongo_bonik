package app.modules.purchase.entity;

import app.common.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

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

  @OneToMany(
   mappedBy = "purchase", cascade = CascadeType.ALL, orphanRemoval = true
  )
  @JsonManagedReference
  private List<PurchaseDetails> details = new ArrayList<>();

}
