package app.modules.base.role.entity;

import app.common.entity.BaseEntity;
import app.common.util.CommonUtil;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="acl_role")
public class Role extends BaseEntity {
    private String authority;
    private String remarks;

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = CommonUtil.removeAllSpace(authority);
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = CommonUtil.removeAllSpace(remarks);
    }
}
