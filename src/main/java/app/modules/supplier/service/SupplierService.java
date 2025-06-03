package app.modules.supplier.service;

import app.common.dto.MsgResponse;
import app.common.dto.ProductDTO;
import app.modules.supplier.repo.SupplierRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SupplierService {

    @Autowired
    private SupplierRepo supplierRepo;

    public MsgResponse create(ProductDTO dto) {

      return null;
    }

    public MsgResponse edit(ProductDTO dto) {

        return null;
    }
}
