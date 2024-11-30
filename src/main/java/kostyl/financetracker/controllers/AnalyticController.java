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
}
