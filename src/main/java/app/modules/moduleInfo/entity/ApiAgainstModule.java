package app.modules.moduleInfo.entity;

import app.common.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
public class ApiAgainstModule extends BaseEntity {

    private String apiPattern; //backendUrl
    private String methodName; //put , post , delete , update , patch
    private String frontUrl;  //frontendUrl

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    @JsonBackReference
    private ModuleInfo moduleInfo;
}
