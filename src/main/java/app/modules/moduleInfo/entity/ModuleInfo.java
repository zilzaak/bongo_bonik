package app.modules.moduleInfo.entity;

import app.common.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
public class ModuleInfo extends BaseEntity {

    private String name;

    private  Long parentModule;

    @OneToMany(mappedBy = "moduleInfo", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<ApiAgainstModule> details = new ArrayList<>();

}
