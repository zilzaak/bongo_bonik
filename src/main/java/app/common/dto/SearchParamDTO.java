package app.common.dto;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SearchParamDTO {
   public Long orgId;
   public Long branchId;
   public Long brandId;
   public Long productId;
   public Long modelId;
   public Long madeWithId;
   public Long sizeId;
   public Long colorId;
   public Long catId;
   public Long uomId;
   public Integer pageNum=1;
   public Integer pageSize=10;
   public String sortDir="desc";
   public String sortField="id";
   public String entity;
   public String commonField;
   private String username;
   private Long roleId;
   private Long moduleId;
   private String menu;
   private String apiPattern;
   private String menuId;
   private Long userId ;
   private String menuDetails;
}
