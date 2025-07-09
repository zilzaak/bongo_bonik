package app.modules.base.moduleInfo.entity;

import app.common.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
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

    @OneToMany(mappedBy = "moduleInfo", cascade = CascadeType.ALL,
            orphanRemoval = true,fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<ApiAgainstModule> details = new ArrayList<>();

}
