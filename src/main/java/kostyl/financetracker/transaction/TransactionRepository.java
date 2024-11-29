package kostyl.financetracker.transaction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "transactions", path = "transactions")
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
