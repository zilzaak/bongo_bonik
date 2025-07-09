package app.modules.base.urlPerm.entity;

import app.common.entity.BaseEntity;
import app.modules.moduleInfo.entity.ApiAgainstModule;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(
        name = "permitted_api",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"api_id", "permitted_module_id"})
        }
)
public class PermittedApi extends BaseEntity{

    @ManyToOne
    @JoinColumn(name = "api_id", nullable = false)
    private ApiAgainstModule api;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    @JsonBackReference
    private PermittedModule permittedModule;
}
