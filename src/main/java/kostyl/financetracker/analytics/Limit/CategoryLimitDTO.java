package kostyl.financetracker.analytics.Limit;

import kostyl.financetracker.transaction.CategoryType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CategoryLimitDTO {
    private CategoryType category;
    private Double limitAmount;
    private Double spentAmount; // Потрачено по категории


}
