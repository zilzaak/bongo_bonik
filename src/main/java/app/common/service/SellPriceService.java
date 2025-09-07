package app.common.service;


import app.common.dto.MsgResponse;
import app.modules.inventory.dto.PricingDTO;
import app.common.entity.SellPrice;
import app.modules.inventory.repo.SellPriceRepo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Service
public class SellPriceService {

    @Autowired
    private SellPriceRepo sellPriceRepo;

    Map<String,Object> validate(PricingDTO dto){
        Map<String,Object> mp = new HashMap<>();
        mp.put("hasError",false);
        if(dto.getEntity()==null || dto.getEntity().isBlank()){
             mp.put("hasError",true);
             mp.put("message","Entity is missing,SellPrice or CostPrice");
             return mp;
        }

        if(dto.getPrice()==null || dto.getOrgId()==null || dto.getProductId()==null){
            mp.put("hasError",true);
            mp.put("message","Unit price , Organization , Product is required field ");
            return mp;
        }

        if(dto.getId()==null){
               if(sellPriceRepo.existsByProductIdAndPrice(dto.getProductId(),dto.getPrice())){
                   mp.put("hasError",true);
                   mp.put("message","Unit price , Organization , Product already exist ");
                   return mp;
               }
        }else{
            if(sellPriceRepo.existsByProductIdAndPriceAndIdNotIn(dto.getProductId(),dto.getPrice(), Arrays.asList(dto.getId()))){
                mp.put("hasError",true);
                mp.put("message","Unit price , Organization , Product already exist ");
                return mp;
            }
        }

        return mp;
    }

    public MsgResponse create(PricingDTO dto) {
        Map<String,Object> mp = validate(dto);
        if((Boolean)mp.get("hasError")){
            return new MsgResponse((String) mp.get("message"),false);
        }

        SellPrice sp = new SellPrice();
        if(dto.getId()!=null){
            sp = sellPriceRepo.findById(dto.getId()).get();
        }
        BeanUtils.copyProperties(dto,sp);
        sellPriceRepo.save(sp);
        return new MsgResponse("Successfully created",true);

    }

    public MsgResponse edit(PricingDTO dto) {
        return create(dto);
    }


}
