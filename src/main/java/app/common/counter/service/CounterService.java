package app.common.counter.service;


import app.common.counter.entity.SystemCounter;
import app.common.counter.repo.SystemCounterRepo;
import app.common.util.CounterEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class CounterService {

    @Autowired
    private SystemCounterRepo counterRepo;
    @Transactional
    public  String getCounterCode(Long orgId , Long branchId , String counterName, String prefix){

        SystemCounter counter=counterRepo.findByNameAndOrgIdAndBranchId(counterName,orgId,branchId);

        if(counter==null){
            counter = new SystemCounter(counterName,orgId,branchId,prefix,10000L,1L);
            counterRepo.save(counter);
            return (counter.getPrefix()+counter.getCurrentNumber());
        }else{
            String nextCode = counter.getPrefix()+(counter.getCurrentNumber()+counter.getIncrement());
            counter.setCurrentNumber(counter.getCurrentNumber()+counter.getIncrement());
            counterRepo.save(counter);
            return nextCode;
        }
    }


    public List<String> getBarCode(String counterName,String prefix, Integer numberOfBarCode,Long orgId , Long branchId) {
        List<String> list = new ArrayList<>();
        SystemCounter counter = counterRepo.findByNameAndOrgIdAndBranchId(counterName,orgId,branchId);
        if(counter==null){
            Long increment=1L;
            Long currentNumber=10000L;
            for(int i=0;i<numberOfBarCode;i++){
                String barCode = prefix+currentNumber;
                list.add(barCode);
                currentNumber = currentNumber + increment;
            }
            counter = new SystemCounter(counterName,orgId,branchId,prefix,currentNumber,1L);
            counterRepo.save(counter);

        }else{
            Long increment=counter.getIncrement();
            Long currentNumber= counter.getCurrentNumber();
            for(int i=0;i<numberOfBarCode;i++){
                String barCode = prefix+currentNumber;
                list.add(barCode);
                currentNumber = currentNumber + increment;
            }
            counter.setCurrentNumber(currentNumber);
            counterRepo.save(counter);
        }

        return list;
    }
}
