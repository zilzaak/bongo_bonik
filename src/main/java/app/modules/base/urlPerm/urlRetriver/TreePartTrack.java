package app.modules.base.urlPerm.urlRetriver;

import lombok.*;

@Setter
@Getter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TreePartTrack {
 public String apiSeq;
 public String parentMenu;
 public String childMenu;
 public Boolean isLastPart;
 public String methodName;
 public String apiUrl;
}
