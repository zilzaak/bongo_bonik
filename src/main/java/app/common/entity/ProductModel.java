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
public class ProductModel extends BaseEntity{
    private String name;
    private Long brandId;
    private String brandName;
    private Long orgId;
    private String orgName;

}
