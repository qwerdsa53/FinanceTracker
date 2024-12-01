package kostyl.financetracker.analytics.Limit;

import kostyl.financetracker.transaction.CategoryType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LimitProgressDTO {
    private CategoryType category;
    private Double progressPercentage; // Прогресс в процентах
}