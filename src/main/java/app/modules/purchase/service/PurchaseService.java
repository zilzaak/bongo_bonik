package app.modules.purchase.service;

import app.common.counter.entity.SystemCounter;
import app.common.counter.service.CounterService;
import app.common.dto.MsgResponse;
import app.common.dto.ProductDTO;
import app.modules.purchase.dto.PurchaseDTO;
import app.modules.purchase.repo.PurchaseRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PurchaseService {

    @Autowired
    private PurchaseRepo purchaseRepo;

    @Autowired
    private CounterService counterService;


    public MsgResponse create(PurchaseDTO dto) {

        return null;
    }

    public MsgResponse edit(PurchaseDTO dto) {

        return null;
    }
}
