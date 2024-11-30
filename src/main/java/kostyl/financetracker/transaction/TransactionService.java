package kostyl.financetracker.transaction;

import jakarta.transaction.Transactional;
import kostyl.financetracker.user.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

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

    @Transactional
    public List<Transaction> findAllForCurrentUser(Long userId) {
        return transactionRepository.findAllForCurrentUser(userId);
    }

    public void addTransactionForCurrentUser(Transaction task, Long userId) {
        User user = new User();
        user.setId(userId);
        task.setUser(user);
        transactionRepository.save(task);
    }

    @Transactional
    public void updateTask(Transaction transaction, Long userId) {
        if (transaction.getId() == null) {
            throw new IllegalArgumentException("Task ID must not be null");
        }
        int updatedRows = transactionRepository.updateTransactionByIdAndUserId(
                transaction.getId(),
                userId,
                transaction.getAmount(),
                transaction.getCategory(),
                transaction.getDescription(),
                transaction.getDate(),
                transaction.getType()
        );

        if (updatedRows == 0) {
            throw new NoSuchElementException("Task not found or does not belong to the user");
        }
    }

    @Transactional
    public void deleteTransaction(Long transactionId, Long userId) {
        try{
            int deleted = transactionRepository.deleteByIdAndUserId(transactionId, userId);
            if (deleted == 0) {
                throw new NoSuchElementException("Task not found or access denied.");
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}