package kostyl.financetracker.analytics;

import kostyl.financetracker.transaction.CategoryType;
import lombok.AllArgsConstructor;


@AllArgsConstructor
public class CategoryStatisticsDTO {
    public CategoryType category;
    public Double amount;

    // Конструктор, геттеры и сеттеры
}
