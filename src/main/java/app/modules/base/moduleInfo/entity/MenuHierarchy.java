package app.modules.base.moduleInfo.entity;

import app.common.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Data
public class MenuHierarchy extends BaseEntity {
    private String frontUrl;  //frontendUrl
    private String menu;
    private String parentMenu;
    private String apiPattern; //backendUrl
    private String methodName; //put , post , delete , update , patch
    private String apiSeq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id") // foreign key column
    @JsonBackReference
    private MenuHierarchy parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<MenuHierarchy> details = new ArrayList<>();
}
