package app.common.counter.entity;

import app.common.entity.BaseEntity;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class SystemCounter extends BaseEntity {

    private String name;
    private Long orgId;
    private Long branchId;
    private String prefix;
    private Long currentNumber;
    private Long increment;

    public SystemCounter(String name, Long orgId, Long branchId, String prefix, Long currentNumber, Long increment) {
        this.name = name;
        this.orgId = orgId;
        this.branchId = branchId;
        this.prefix = prefix;
        this.currentNumber = currentNumber;
        this.increment = increment;
    }
}
