package kostyl.financetracker.controllers;

import kostyl.financetracker.analytics.AnalyticService;
import kostyl.financetracker.security.CustomUserDetails;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("api/v1/analytic")
@AllArgsConstructor
public class AnalyticController {

    private final AnalyticService analyticService;

    private Long getUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getPrincipal() instanceof CustomUserDetails userDetails) {
            return userDetails.getId();
        } else {
            throw new IllegalStateException("Authentication principal is not an instance of CustomUserDetails");
        }
    }

    @GetMapping("/total")
    public Dictionary<String,Object> getTotalAnalytic(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {

        Dictionary<String, Object> totalAnalytic = new Hashtable<>();
        Long userId = getUserId();
        // Basic data by User
        try {
            totalAnalytic.put("totalAmount", analyticService.getTotalAmountByUser(userId));
        } catch (Exception e) {
            System.out.println("AAA1");
            System.out.println(e.getMessage());
            totalAnalytic.put("totalAmount",0);
        }
        totalAnalytic.put("totalIncome", analyticService.getTotalIncomeByUser(userId));
        totalAnalytic.put("totalExpenses", analyticService.getTotalExpensesByUser(userId));
        totalAnalytic.put("balance", analyticService.getUserBalance(userId));

        // Data by Category
        totalAnalytic.put("totalByCategory", analyticService.getTotalAmountByCategory(userId));
        totalAnalytic.put("expensesByCategory", analyticService.getExpensesByCategory(userId));
        totalAnalytic.put("incomeByCategory", analyticService.getIncomeByCategory(userId));
        //totalAnalytic.put("categorySummary", analyticService.getCategorySummary(userId));

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
