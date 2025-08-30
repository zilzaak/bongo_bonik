package app.common.entity;

import app.modules.base.org.entity.Organization;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
    @ManyToOne
    private Brand brand;
    @ManyToOne
    private Organization org;
}
