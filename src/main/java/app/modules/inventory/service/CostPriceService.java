package app.modules.inventory.service;

import app.common.dto.MsgResponse;
import app.modules.inventory.dto.PricingDTO;
import app.modules.inventory.repo.CostPriceRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CostPriceService {

    @Autowired
    private CostPriceRepo costPriceRepo;

    public MsgResponse create(PricingDTO dto) {

        return null;
    }

    public MsgResponse edit(PricingDTO dto) {

        return create(dto);
    }
}
