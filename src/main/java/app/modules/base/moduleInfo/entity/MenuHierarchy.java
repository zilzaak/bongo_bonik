package app.modules.base.moduleInfo.entity;

import app.common.entity.BaseEntity;
import app.modules.base.urlPerm.entity.PermittedApi;
import com.fasterxml.jackson.annotation.JsonBackReference;
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
public class MenuHierarchy extends BaseEntity {

    private String apiPattern; //backendUrl
    private String methodName; //put , post , delete , update , patch
    private String frontUrl;  //frontendUrl
    private String type; //module , sub-module , menu
    private String name;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "parent_id") // Explicit join column instead of mappedBy
    @JsonManagedReference
    private List<MenuHierarchy> details = new ArrayList<>();
}
