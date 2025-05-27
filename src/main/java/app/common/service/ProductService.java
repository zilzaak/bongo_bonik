package app.common.service;

import app.common.dto.CommonDTO;
import app.common.dto.MsgResponse;
import app.common.dto.ProductDTO;
import app.common.repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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


    public MsgResponse create(ProductDTO dto) {



        return null;
    }

    public MsgResponse edit(ProductDTO dto) {

        return null;
    }
}
