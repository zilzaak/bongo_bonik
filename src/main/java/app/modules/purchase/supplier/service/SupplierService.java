package app.modules.purchase.supplier.service;

import app.common.dto.MsgResponse;
import app.common.dto.SearchParamDTO;
import app.common.util.CommonUtil;
import app.modules.purchase.supplier.dto.SupplierDTO;
import app.modules.purchase.supplier.dto.SupplierData;
import app.modules.purchase.supplier.entity.Supplier;
import app.modules.purchase.supplier.repo.SupplierRepo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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

        if(!CommonUtil.validUserOrg(dto.getOrgId())){
            mp.put("hasError",true);
            mp.put("message","The organization is invalid ");
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

    public Supplier getById(Long supplierId) {
        return supplierRepo.findById(supplierId).orElse(null);
    }

    public MsgResponse getList(SearchParamDTO dto) {
        Pageable pageable= CommonUtil.getPageable(dto);

        if(dto.getDropDown()!=null && CommonUtil.validUserOrg(dto.orgId)){
            Page<Object> page=supplierRepo.getList(dto.getOrgId(),pageable);
            return CommonUtil.responseFromObjectPage(page);
        }

        if(dto.getId()==null && !CommonUtil.validUserOrg(dto.orgId)){
           return new MsgResponse("Invalid user organization",false);
        }

        Page<Object> page=supplierRepo.getList(dto.getId(),dto.getOrgId(),dto.getCommonField()!=null?dto.getCommonField().toUpperCase():null,pageable);
        MsgResponse resp = CommonUtil.responseFromObjectPage(page);
        if(dto.getId()!=null){
            Map<String,Object> mp = (Map<String, Object>) resp.getData();
          if(!CommonUtil.validUserOrg(((List<SupplierData>)mp.get("listData")).get(0).getOrgId())){
              return new MsgResponse("Invalid user organization",false);
          }
        }
        return resp;
    }
}
