package kostyl.financetracker.controllers;

import kostyl.financetracker.analytics.AnalyticService;
import kostyl.financetracker.analytics.CategoryStatisticsDTO;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("api/v1/analytic")
@AllArgsConstructor
public class AnalyticController {

    private final AnalyticService analyticService;

    // Общая сумма транзакций пользователя
    @GetMapping("/total/{userId}")
    public Double getTotalAmountByUser(@PathVariable Long userId) {
        return analyticService.getTotalAmountByUser(userId);
    }

    @GetMapping("/income/{userId}")
    public Double getTotalIncomeByUser(@PathVariable Long userId){
        return analyticService.getTotalIncomeByUser(userId);
    }

    @GetMapping("/expenses/{userId}")
    public Double getTotalExpensesByUser(@PathVariable Long userId){
        return analyticService.getTotalExpensesByUser(userId);
    }

    @GetMapping("/balance/{userId}")
    public Double getUserBalance(@PathVariable Long userId) { // Доходы - Расходы
        return analyticService.getUserBalance(userId);
    }

    // Количество транзакций за период
    @GetMapping("/count")
    public Long getTransactionCountByDateRange(@RequestParam String startDate,
                                               @RequestParam String endDate) {
        return analyticService.getTransactionCountByDateRange(
                LocalDate.parse(startDate), LocalDate.parse(endDate));
    }

    // Общая сумма по категориям
    @GetMapping("/category/{userId}")
    public List<CategoryStatisticsDTO> getTotalAmountByCategory(@PathVariable Long userId) {
        return analyticService.getTotalAmountByCategory(userId);
    }

    @GetMapping("/expenses/categories/{userId}")
    public List<CategoryStatisticsDTO> getExpensesByCategory(@PathVariable Long userId) {
        return analyticService.getExpensesByCategory(userId);
    }

    @GetMapping("/income/categories/{userId}")
    public List<CategoryStatisticsDTO> getIncomeByCategory(@PathVariable Long userId) {
        return analyticService.getIncomeByCategory(userId);
    }

    @GetMapping("/categories/summary/{userId}")
    public CategoryStatisticsDTO getCategorySummary(@PathVariable Long userId) {
        return analyticService.getCategorySummary(userId);
    }

    // Транзакции пользователя за период
    @GetMapping("/transactions/{userId}")
    public List<Object[]> getTransactionsByUserAndDateRange(
            @PathVariable Long userId,
            @RequestParam String startDate,
            @RequestParam String endDate) {
        return analyticService.getTransactionsByUserAndDateRange(
                userId, LocalDate.parse(startDate), LocalDate.parse(endDate));
    }

    // Сумма расходов пользователя за неделю/месяц
    @GetMapping("/expenses_time/{userId}")
    public Double getTotalExpensesByDateRange(
            @PathVariable Long userId,
            @RequestParam String startDate,
            @RequestParam String endDate) {
        return analyticService.getTotalExpensesByUserAndDateRange(
                userId, LocalDate.parse(startDate), LocalDate.parse(endDate));
    }

    // Статистика доходов/расходов по категориям за период
    @GetMapping("/statistics/{userId}")
    public List<CategoryStatisticsDTO> getCategoryStatisticsByDateRange(
            @PathVariable Long userId,
            @RequestParam String startDate,
            @RequestParam String endDate) {
        return analyticService.getCategoryStatisticsByDateRange(
                userId, LocalDate.parse(startDate), LocalDate.parse(endDate));
    }
}
