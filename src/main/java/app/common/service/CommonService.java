package app.common.service;


import app.common.repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommonService {

   @Autowired
   private BrandRepo brandRepo;
    @Autowired
    private ProductCatRepo catRepo;
    @Autowired
    private ProductModelRepo modelRepo;
    @Autowired
    private ProductSizeRepo sizeRepo;
    @Autowired
    private ProductColorRepo colorRepo;







}
