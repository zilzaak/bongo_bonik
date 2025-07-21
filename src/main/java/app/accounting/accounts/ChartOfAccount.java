package app.accounting.accounts;

import app.common.entity.BaseEntity;
import app.common.entity.Branch;
import app.common.entity.Organization;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ChartOfAccount extends BaseEntity {

    @ManyToOne
    private Organization org;

    @ManyToOne
    private Branch branch;

    private String accName;

    @ManyToOne
    private ChartOfAccount parent;

    private String accountType;

}
