package app.common.counter.service;


import app.common.counter.entity.SystemCounter;
import app.common.counter.repo.SystemCounterRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CounterService {

    @Autowired
    private SystemCounterRepo counterRepo;

    public  String getCounterCode(Long orgId , Long branchId , String counterName, String prefix){

        SystemCounter counter = counterRepo.findByNameAndOrgIdAndBranchId(counterName,orgId,branchId);
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


}
