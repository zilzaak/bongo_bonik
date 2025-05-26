package app.common.entity;

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
public class UnitOfMeasure extends BaseEntity{
    private String name;
    private String productCatIds;
    private Long orgId;
    private String orgName;
}
