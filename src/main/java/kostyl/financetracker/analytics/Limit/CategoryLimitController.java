package kostyl.financetracker.analytics.Limit;

import kostyl.financetracker.transaction.CategoryType;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("api/v1/category-limits")
@AllArgsConstructor
public class CategoryLimitController {

    private final CategoryLimitService categoryLimitService;

    @GetMapping("/{userId}")
    public List<CategoryLimit> getLimits(@PathVariable Long userId) {
        return categoryLimitService.getAllLimitsByUser(userId);
    }

    @PostMapping("/{userId}/{category}")
    public CategoryLimit setLimit(@PathVariable Long userId,
                                  @PathVariable CategoryType category,
                                  @RequestParam Double limitAmount) {
        return categoryLimitService.setLimit(userId, category, limitAmount);
    }

    @GetMapping("/{userId}/progress")
    public Double getLimitProgress(@PathVariable Long userId,
                                   @RequestParam CategoryType category) {
        return categoryLimitService.getLimitProgress(userId, category);
    }
}
