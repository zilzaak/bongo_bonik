package app.common.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
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
}
