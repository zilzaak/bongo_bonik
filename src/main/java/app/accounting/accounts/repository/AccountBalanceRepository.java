package app.accounting.accounts.repository;

import app.accounting.accounts.entity.AccountBalance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountBalanceRepository extends JpaRepository<AccountBalance,Long> {

}
