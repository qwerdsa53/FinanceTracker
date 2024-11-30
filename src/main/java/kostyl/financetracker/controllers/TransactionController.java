package kostyl.financetracker.controllers;
import kostyl.financetracker.transaction.TransactionService;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    private final TransactionService transactionService;
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }
    // общее количество транзакций пользователя
    @GetMapping("/total/{userId}")
    public Double getTotalAmountByUser(@PathVariable Long userId) {
        return transactionService.getTotalAmountByUser(userId);
    }
    // общее количество транзакций за период
    @GetMapping("/count")
    public Long getTransactionCountByDateRange(@RequestParam String startDate,
                                               @RequestParam String endDate) {
        return transactionService.getTransactionCountByDateRange(
                LocalDate.parse(startDate), LocalDate.parse(endDate));
    }
    @GetMapping("/category/{userId}")
    @Query("SELECT t.category, SUM(t.amount) FROM Transaction t WHERE t.user.id = :userId GROUP BY t.category")
    public List<Object[]> getTotalAmountByCategory(@PathVariable Long userId) {
        return transactionService.getTotalAmountByCategory(userId);
    }
}