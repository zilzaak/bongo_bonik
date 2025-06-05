package app.modules.inventory.service;


import app.modules.inventory.entity.PdctBarCode;
import app.modules.inventory.entity.StockBalance;
import app.modules.inventory.repo.BarCodeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PdctBarCodeService {

@Autowired
private BarCodeRepo barCodeRepo;

    public void saveNewBarcodes(List<String> barCodes, StockBalance balance) {
        List<PdctBarCode> list = new ArrayList<>();
        for(String code : barCodes){
            PdctBarCode obj = new PdctBarCode();
            obj.setBarCode(code);
            obj.setStockBal(balance);
            list.add(obj);
        }
        barCodeRepo.saveAll(list);
    }
}
