package app;

import app.common.repo.MadeWithRepo;
import app.common.repo.ProductColorRepo;
import app.common.repo.ProductModelRepo;
import app.common.repo.ProductSizeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BazarSodaiApplication implements CommandLineRunner {
    @Autowired
    private ProductModelRepo modelRepo;
    @Autowired
    private ProductSizeRepo sizeRepo;
    @Autowired
    private ProductColorRepo colorRepo;
    @Autowired
    private MadeWithRepo madeWithRepo;


    public static void main(String[] args){

        SpringApplication.run(BazarSodaiApplication.class,args);

    }

    @Override
    public void run(String... args) throws Exception {

    }
}
