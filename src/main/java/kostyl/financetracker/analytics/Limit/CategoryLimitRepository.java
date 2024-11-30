package kostyl.financetracker.analytics.Limit;

import kostyl.financetracker.transaction.CategoryType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoryLimitRepository extends JpaRepository<CategoryLimit, Long> {

    List<CategoryLimit> findByUserId(Long userId);

    @Query("SELECT cl FROM CategoryLimit cl WHERE cl.user.id = :userId AND cl.category = :category")
    CategoryLimit findByUserIdAndCategory(@Param("userId") Long userId, @Param("category") CategoryType category);
}
