package kostyl.financetracker.analytics.Limit;

import kostyl.financetracker.transaction.CategoryType;
import kostyl.financetracker.transaction.Transaction;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/category-limits")
@AllArgsConstructor
public class CategoryLimitController {

    private final CategoryLimitService categoryLimitService;

    // Получить все лимиты пользователя
    @GetMapping("/{userId}")
    public ResponseEntity<List<CategoryLimitDTO>> getLimits(@PathVariable Long userId) {
        List<CategoryLimitDTO> limits = categoryLimitService.getAllLimitsByUser(userId);
        if (limits.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(limits);
    }

    // Установить лимит для категории
    @PostMapping("/{userId}/{category}")
    public ResponseEntity<CategoryLimit> setLimit(@PathVariable Long userId,
                                                  @PathVariable CategoryType category,
                                                  @RequestParam Double limitAmount) {
        try {
            CategoryLimit limit = categoryLimitService.setLimit(userId, category, limitAmount);
            return ResponseEntity.ok(limit);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null); // Возвращаем ошибку в случае недопустимого лимита
        }
    }

    // Получить прогресс по лимиту в категории
    @GetMapping("/{userId}/progress")
    public ResponseEntity<Double> getLimitProgress(@PathVariable Long userId,
                                                   @RequestParam CategoryType category) {
        try {
            Double progress = categoryLimitService.getLimitProgress(userId, category);
            return ResponseEntity.ok(progress);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null); // Возвращаем ошибку, если лимит не найден
        }
    }

    // Проверить лимит для категории
    @GetMapping("/{userId}/{category}")
    public ResponseEntity<CategoryLimit> getLimitByCategory(@PathVariable Long userId,
                                                            @PathVariable CategoryType category) {
        CategoryLimit limit = categoryLimitService.getLimitByCategory(userId, category);
        if (limit == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(limit);
    }

    // Обновить потраченные средства после транзакции
    @PostMapping("/update-spent")
    public ResponseEntity<String> updateSpentAmount(@RequestBody Transaction transaction) {
        try {
            categoryLimitService.updateSpentAmount(transaction);
            return ResponseEntity.ok("Spent amount updated successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Error updating spent amount: " + e.getMessage());
        }
    }
}
