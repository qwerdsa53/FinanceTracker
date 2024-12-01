package kostyl.financetracker.controllers;

import kostyl.financetracker.analytics.AnalyticService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Dictionary;
import java.util.Hashtable;

@RestController
@RequestMapping("api/v1/analytic")
@AllArgsConstructor
public class AnalyticController {

    private final AnalyticService analyticService;

    @GetMapping("/total/{userId}")
    public Dictionary<String, Object> getTotalAnalytic(
            @PathVariable Long userId,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {

        Dictionary<String, Object> totalAnalytic = new Hashtable<>();

        // Basic data by User
        try {
            totalAnalytic.put("totalAmount", analyticService.getTotalAmountByUser(userId));
        } catch (Exception e) {
            System.out.println("AAA1");
            e.printStackTrace();
            totalAnalytic.put("totalAmount", 0);
        }
        totalAnalytic.put("totalIncome", analyticService.getTotalIncomeByUser(userId));
        totalAnalytic.put("totalExpenses", analyticService.getTotalExpensesByUser(userId));
        totalAnalytic.put("balance", analyticService.getUserBalance(userId));

        // Data by Category
        totalAnalytic.put("totalByCategory", analyticService.getTotalAmountByCategory(userId));
        totalAnalytic.put("expensesByCategory", analyticService.getExpensesByCategory(userId));
        totalAnalytic.put("incomeByCategory", analyticService.getIncomeByCategory(userId));
        try {
            totalAnalytic.put("categorySummary", analyticService.getCategorySummary(userId));
        } catch (Exception e) {
            System.out.println("AAA2");
            e.printStackTrace();
            totalAnalytic.put("categorySummary", 0);
        }
        // Data based on Date
        if (startDate != null && endDate != null) {
            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);

            totalAnalytic.put("transactionCount", analyticService.getTransactionCountByDateRange(start, end));
            totalAnalytic.put("transactions", analyticService.getTransactionsByUserAndDateRange(userId, start, end));
            totalAnalytic.put("expensesByDateRange", analyticService.getTotalExpensesByUserAndDateRange(userId, start, end));
            totalAnalytic.put("categoryStatisticsByDateRange", analyticService.getCategoryStatisticsByDateRange(userId, start, end));
        }

        return totalAnalytic;
    }

}
