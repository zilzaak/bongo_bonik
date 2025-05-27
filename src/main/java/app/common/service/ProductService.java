package app.common.service;

import app.common.dto.CommonDTO;
import app.common.dto.MsgResponse;
import app.common.dto.ProductDTO;
import app.common.repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ProductService {

     @Autowired
     private BrandRepo brandRepo;
    @Autowired
    private ProductModelRepo modelRepo;

    @Autowired
    private ProductCatRepo catRepo;

    @Autowired
    private ProductSizeRepo sizeRepo;

    @Autowired
    private ProductColorRepo colorRepo;

    @Autowired
    private UnitOfMeasureRepo uomRepo;
     // make default color->random color
    //1>>first check required field are null
    //2>> now check for
    // Electronics required (model no,color,size),for Pharmacy,Grocery(qtyPerUnit,uom) ,for Clothing,shows,sandal(color, size) ,
    //

      Map<String,Object> validate(ProductDTO dto){
          Map<String,Object> mp = new HashMap<>();
          mp.put("hasError",false);

          if(dto.getOrgId()==null || dto.getCatId()==null || dto.getBrandId()==null){
           mp.put("hasError",true);
           mp.put("message","Organization , category , brand is required ");
           return mp;
          }

          return mp;
      }

    public MsgResponse create(ProductDTO dto) {



        return null;
    }

    public MsgResponse edit(ProductDTO dto) {

        return null;
    }
}
