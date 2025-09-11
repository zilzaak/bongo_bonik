package app.modules.purchase.entity;

import app.common.entity.BaseEntity;
import app.common.entity.Branch;
import app.modules.base.org.entity.Organization;
import app.modules.inventory.entity.Inventory;
import app.modules.purchase.supplier.entity.Supplier;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
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

  private String code;

  @Column(nullable = false)
  private Long orgId;

  @Column(nullable = false)
  private Long branchId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(nullable = false)
  private Inventory inventory;
  @ManyToOne(fetch = FetchType.LAZY)
  private Supplier supplier;

  private Double totalBill;
  private Double dueAmount;


  @OneToMany(mappedBy = "purchase", cascade = CascadeType.ALL, orphanRemoval = true)
  @JsonManagedReference
  private List<PurchaseDetails> details = new ArrayList<>();

}
