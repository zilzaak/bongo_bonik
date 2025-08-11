package app.accounting.accounts.repository;

import app.accounting.accounts.entity.PeriodWiseAccBal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PeriodWiseAccBalRepository extends JpaRepository<Long, PeriodWiseAccBal> {


}
