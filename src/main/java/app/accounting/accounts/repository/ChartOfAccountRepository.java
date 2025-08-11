package app.accounting.accounts.repository;

import app.accounting.accounts.entity.ChartOfAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChartOfAccountRepository extends JpaRepository<ChartOfAccount,Long> {
}
