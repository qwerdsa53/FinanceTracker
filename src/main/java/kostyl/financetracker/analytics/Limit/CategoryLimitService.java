package kostyl.financetracker.analytics.Limit;

import kostyl.financetracker.transaction.CategoryType;
import kostyl.financetracker.transaction.Transaction;
import kostyl.financetracker.user.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CategoryLimitService {

    private final CategoryLimitRepository categoryLimitRepository;

    public List<CategoryLimitDTO> getAllLimitsByUser(Long userId) {
        List<CategoryLimit> limits = categoryLimitRepository.findByUserId(userId);
        return limits.stream()
                .map(limit -> new CategoryLimitDTO(limit.getCategory(), limit.getLimitAmount(), limit.getSpentAmount()))
                .collect(Collectors.toList());
    }


    public CategoryLimit getLimitByCategory(Long userId, CategoryType category) {
        return categoryLimitRepository.findByUserIdAndCategory(userId, category);
    }

    public CategoryLimit setLimit(Long userId, CategoryType category, Double limitAmount) {
        CategoryLimit categoryLimit = categoryLimitRepository.findByUserIdAndCategory(userId, category);
        if (categoryLimit == null) {
            categoryLimit = new CategoryLimit();
            User user = new User();
            user.setId(userId);
            categoryLimit.setUser(user); // Предполагается, что User(id) существует
            categoryLimit.setCategory(category);
        }
        categoryLimit.setLimitAmount(limitAmount);
        return categoryLimitRepository.save(categoryLimit);
    }

    public void updateSpentAmount(Transaction transaction) {
        CategoryLimit categoryLimit = categoryLimitRepository.findByUserIdAndCategory(
                transaction.getUser().getId(),
                transaction.getCategory()
        );

        if (categoryLimit != null) {
            categoryLimit.setSpentAmount(categoryLimit.getSpentAmount() + transaction.getAmount());
            categoryLimitRepository.save(categoryLimit);
        }
    }

    public Double getLimitProgress(Long userId, CategoryType category) {
        CategoryLimit categoryLimit = categoryLimitRepository.findByUserIdAndCategory(userId, category);
        if (categoryLimit == null || categoryLimit.getLimitAmount() == null) {
            throw new IllegalArgumentException("Лимит не задан для этой категории");
        }
        return (categoryLimit.getSpentAmount() / categoryLimit.getLimitAmount()) * 100; // Прогресс в процентах
    }

}
