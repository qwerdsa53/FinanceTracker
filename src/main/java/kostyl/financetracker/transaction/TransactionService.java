package kostyl.financetracker.transaction;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
@Service
@AllArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;
    public Double getTotalAmountByUser(Long userId) {
        return transactionRepository.getTotalAmountByUser(userId);
    }
    public Long getTransactionCountByDateRange(LocalDate startDate, LocalDate endDate) {
        return transactionRepository.getTransactionCountByDateRange(startDate, endDate);
    }
    public List<Object[]> getTotalAmountByCategory(Long userId) {
        return transactionRepository.getTotalAmountByCategory(userId);
    }
}