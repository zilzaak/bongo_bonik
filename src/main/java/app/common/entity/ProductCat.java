package app.common.entity;

import jakarta.persistence.Entity;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
public class ProductCat extends BaseEntity {

    private String title;
    private Long parentId;

}
