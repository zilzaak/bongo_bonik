package app.modules.base.urlPerm.dto;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class PrmttedApiDTO {
    private Long id;
    private String backendUrl;
    private String frontendUrl;
    private Long user;
    private Long role;
    private String menuIdsHierarchy;
}
