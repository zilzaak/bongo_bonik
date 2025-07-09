package app.modules.sales.customer.service;


import app.common.dto.MsgResponse;
import app.modules.sales.customer.dto.CustomerDTO;
import app.modules.sales.customer.entity.Customer;
import app.modules.sales.customer.repo.CustomerRepo;
import app.common.entity.Branch;
import app.common.repo.BranchRepo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepo customerRepo;
    @Autowired
    private BranchRepo branchRepo;

    Map<String,Object> validate(CustomerDTO dto){
        Map<String,Object> mp = new HashMap<>();
        mp.put("hasError",false);

        if(dto.getOrgId()==null || dto.getName()==null || dto.getName().isEmpty() || dto.getPhone()==null || dto.getPhone().isEmpty()){
            mp.put("hasError",true);
            mp.put("message","Organization , Name , Phone is required field ");
            return mp;
        }

        Long number=null;
        try{
            number=Long.parseLong(dto.getPhone());
        }catch (Exception e){
            mp.put("hasError",true);
            mp.put("message","Invalid phone number ");
            return mp;
        }

        if(dto.getBranchId()!=null){
            Branch branch = branchRepo.findById(dto.getBranchId()).get();
            if(!dto.getOrgId().equals(branch.getOrg().getId())){
                mp.put("hasError",true);
                mp.put("message","Selected branch is not under the organization you selected , choose correct branch");
                return mp;
            }
        }

        if(dto.getId()==null){
            if(customerRepo.existsByOrgIdAndPhone(dto.getOrgId(),dto.getPhone())){
                mp.put("hasError",true);
                mp.put("message","Customer against contact no"+dto.getPhone()+" already exist");
                return mp;
            }
        }else{
            if(customerRepo.existsByOrgIdAndPhoneAndIdNotIn(dto.getOrgId(),dto.getPhone(), Arrays.asList(dto.getId()))){
                mp.put("hasError",true);
                mp.put("message","Customer against contact no"+dto.getPhone()+" already exist");
                return mp;
            }
        }

        return mp;

    }

    public MsgResponse create(CustomerDTO dto) {
        Map<String,Object> mp = validate(dto);
        if((boolean)mp.get("hasError")){
            return new MsgResponse((String)mp.get("message"),false);
        }
        Customer customer = new Customer();
        BeanUtils.copyProperties(dto,customer);
        customerRepo.save(customer);
        return new MsgResponse("Successfully Created Customer",true);
    }

    public MsgResponse  edit(CustomerDTO dto) {
           return create(dto);
    }
}
