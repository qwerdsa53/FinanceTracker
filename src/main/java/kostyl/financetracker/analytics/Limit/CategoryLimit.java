package kostyl.financetracker.analytics.Limit;

import jakarta.persistence.*;
import kostyl.financetracker.transaction.CategoryType;
import kostyl.financetracker.user.User;
import lombok.Data;

@Entity
@Data
@Table(name = "limits")
public class CategoryLimit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private CategoryType category;

    private Double limitAmount;

    private Double spentAmount; // Обновляется при каждой новой транзакции
}
