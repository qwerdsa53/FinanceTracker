package kostyl.financetracker.transaction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.time.LocalDate;
import java.util.List;

@RepositoryRestResource(collectionResourceRel = "transactions", path = "transactions")
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    // Общая сумма транзакций пользователя
    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.user.id = :userId")
    Double getTotalAmountByUser(@Param("userId") Long userId);

    // Общая сумма доходов и расходов пользователя
    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.user.id = :userId AND t.type = :type")
    Double getTotalAmountByUserAndType(@Param("userId") Long userId,
                                       @Param("type") TransactionType type);

    // Количество транзакций за период
    @Query("SELECT COUNT(t) FROM Transaction t WHERE t.date BETWEEN :startDate AND :endDate")
    Long getTransactionCountByDateRange(@Param("startDate") LocalDate startDate,
                                        @Param("endDate") LocalDate endDate);

    // Сумма по категориям
    @Query("SELECT t.category, SUM(t.amount) FROM Transaction t WHERE t.user.id = :userId GROUP BY t.category")
    List<Object[]> getTotalAmountByCategory(@Param("userId") Long userId);

    @Query("SELECT t.category, SUM(t.amount) FROM Transaction t WHERE t.user.id = :userId AND t.type = :type GROUP BY t.category")
    List<Object[]> getAmountByCategory(@Param("userId") Long userId,
                                       @Param("type") TransactionType type);
}
