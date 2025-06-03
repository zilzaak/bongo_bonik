package app.modules.supplier.service;

import app.common.dto.MsgResponse;
import app.common.dto.ProductDTO;
import app.modules.supplier.dto.SupplierDTO;
import app.modules.supplier.entity.Supplier;
import app.modules.supplier.repo.SupplierRepo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Service
public class SupplierService {

    @Autowired
    private SupplierRepo supplierRepo;

    Map<String,Object> validate(SupplierDTO dto){

        Map<String,Object> mp = new HashMap<>();
        mp.put("hasError",false);

        if(dto.getName()==null || dto.getPhone()==null ||
           dto.getOrgId()==null||dto.getName().isEmpty()
            || dto.getPhone().isEmpty()){
            mp.put("hasError",true);
            mp.put("message","Name , phone , organization is required ");
            return mp;
        }

        if(dto.getId()==null){
            if(supplierRepo.existsByPhoneAndOrgId(dto.getPhone(),dto.getOrgId())){
                mp.put("hasError",true);
                mp.put("message","this supplier already exist against selected organization");
                return mp;
            }
        }else{
            if(supplierRepo.existsByPhoneAndOrgIdAndIdNotIn(dto.getPhone(),dto.getOrgId(), Arrays.asList(dto.getId()))){
                mp.put("hasError",true);
                mp.put("message","this supplier already exist against selected organization");
                return mp;
            }
        }
        return mp;
    }

    public MsgResponse create(SupplierDTO dto) {
        Map<String,Object> mp = validate(dto);
        if((boolean) mp.get("hasError")){
            return new MsgResponse((String)mp.get("message"),false);
        }
        Supplier supplier = new Supplier();
        if(dto.getId()!=null){
            supplier = supplierRepo.findById(dto.getId()).get();
        }
        BeanUtils.copyProperties(dto,supplier);
        supplierRepo.save(supplier);
      return new MsgResponse("Successfully created",true);
    }

    public MsgResponse edit(SupplierDTO dto) {
        return create(dto);
    }
}
