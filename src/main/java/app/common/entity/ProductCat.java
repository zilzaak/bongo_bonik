package app.common.entity;

import jakarta.persistence.Entity;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
public class ProductCat extends BaseEntity {

    private Long orgId;
    private String orgName;

    private String name;
    private Long parentId;
    private String description;

}
