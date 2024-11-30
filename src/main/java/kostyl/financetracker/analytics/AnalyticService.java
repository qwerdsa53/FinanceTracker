package kostyl.financetracker.analytics;

import kostyl.financetracker.transaction.CategoryType;
import kostyl.financetracker.transaction.TransactionRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static kostyl.financetracker.transaction.TransactionType.EXPENSE;
import static kostyl.financetracker.transaction.TransactionType.INCOME;


@Service
@AllArgsConstructor
@Slf4j
public class AnalyticService {

    private final TransactionRepository transactionRepository;

    // Общая сумма транзакций пользователя
    public Double getTotalAmountByUser(Long userId) {
        return transactionRepository.getTotalAmountByUser(userId);
    }

    public Double getTotalIncomeByUser(Long userId) {
        Double incomes = transactionRepository.getTotalAmountByUserAndType(userId,INCOME);

        if (incomes == null ) { incomes = 0.0; }
        return incomes;
    }

    public Double getTotalExpensesByUser(Long userId) {
        Double expenses = transactionRepository.getTotalAmountByUserAndType(userId,EXPENSE);
        if (expenses == null) { expenses = 0.0; }
        return expenses;
    }

    public Double getUserBalance(Long userId) {
        Double incomes = transactionRepository.getTotalAmountByUserAndType(userId,INCOME);
        Double expenses = transactionRepository.getTotalAmountByUserAndType(userId,EXPENSE);
        if (incomes == null ) {
            incomes = 0.0;
        }
        if (expenses == null ) {
            expenses = 0.0;
        }
        return incomes - expenses;
    }

    // Количество транзакций за период
    public Long getTransactionCountByDateRange(LocalDate startDate, LocalDate endDate) {
        return transactionRepository.getTransactionCountByDateRange(startDate, endDate);
    }

    // Общая сумма по категориям (возвращаем DTO)

    public List<CategoryStatisticsDTO> getTotalAmountByCategory(Long userId) {
        List<CategoryStatisticsDTO> resultAll = transactionRepository.getTotalAmountByCategory(userId)
                .stream()
                .map(result -> new CategoryStatisticsDTO((CategoryType) result[0], (Double) result[1]))
                .collect(Collectors.toList());
        return resultAll;
    }

    public List<CategoryStatisticsDTO> getExpensesByCategory(Long userId) {
        List<CategoryStatisticsDTO> resultAll = transactionRepository.getAmountByCategory(userId, EXPENSE)
                .stream()
                .map(result -> new CategoryStatisticsDTO((CategoryType) result[0], (Double) result[1]))
                .collect(Collectors.toList());
        return resultAll;
    }

    public List<CategoryStatisticsDTO> getIncomeByCategory(Long userId) {
        List<CategoryStatisticsDTO> resultAll = transactionRepository.getAmountByCategory(userId, INCOME)
                .stream()
                .map(result -> new CategoryStatisticsDTO((CategoryType) result[0], (Double) result[1]))
                .collect(Collectors.toList());
        return resultAll;
    }

    public CategoryStatisticsDTO getCategorySummary(Long userId) {
        return null;
    }

    // Все транзакции пользователя за период
    public List<Object[]> getTransactionsByUserAndDateRange(Long userId, LocalDate startDate, LocalDate endDate) {
        return transactionRepository.getTransactionsByUserAndDateRange(userId, startDate, endDate);
    }

    // Сумма расходов пользователя за неделю/месяц
    public Double getTotalExpensesByUserAndDateRange(Long userId, LocalDate startDate, LocalDate endDate) {
        Double totalExpenses = transactionRepository.getTotalExpensesByUserAndDateRange(userId, startDate, endDate);
        return totalExpenses != null ? totalExpenses : 0.0;
    }

    // Статистика доходов/расходов по категориям за период
    public List<CategoryStatisticsDTO> getCategoryStatisticsByDateRange(Long userId, LocalDate startDate, LocalDate endDate) {
        return transactionRepository.getCategoryStatisticsByDateRange(userId, startDate, endDate)
                .stream()
                .map(result -> new CategoryStatisticsDTO(
                        (CategoryType) result[0],  // Category
                        (Double) result[1]  // amount
                ))
                .collect(Collectors.toList());
    }

}
