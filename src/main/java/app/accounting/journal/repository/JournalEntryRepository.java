package app.accounting.journal.repository;
import app.accounting.journal.entity.JournalEntry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JournalEntryRepository extends JpaRepository<JournalEntry,Long> {
}
