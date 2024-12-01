package kostyl.financetracker.controllers;

import kostyl.financetracker.security.CustomUserDetails;
import kostyl.financetracker.transaction.CategoryType;
import kostyl.financetracker.transaction.Transaction;
import kostyl.financetracker.transaction.TransactionService;
import kostyl.financetracker.transaction.TransactionType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping("/all")
    public List<Transaction> findAllForCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getPrincipal() instanceof CustomUserDetails userDetails) {
            Long userId = userDetails.getId();
            return transactionService.findAllForCurrentUser(userId);
        } else {
            throw new IllegalStateException("Authentication principal is not an instance of CustomUserDetails");
        }
    }

    @PostMapping
    public ResponseEntity<String> addTransactionForCurrentUser(@RequestBody Transaction transaction) {
        Long userId = getUserId();
        log.info("Tr: {}", transaction);
        try {
            transactionService.addTransactionForCurrentUser(transaction, userId);
            return ResponseEntity.status(HttpStatus.CREATED).body("Task created successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid input: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating task: " + e.getMessage());
        }

    }

    @PutMapping
    public ResponseEntity<String> updateTransaction(@RequestBody Transaction task) {
        Long userId = getUserId();
        try {
            transactionService.updateTransaction(task, userId);
            return ResponseEntity.status(HttpStatus.OK).body("Task updated successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid input: " + e.getMessage());
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task not found.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating task: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<Page<Transaction>> getTransactions(
            @RequestParam int page,
            @RequestParam(defaultValue = "40") int size,
            @RequestParam(defaultValue = "desc") String sortDirection,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String category) {

        Sort sort = Sort.by(Sort.Order.desc("date"));
        Pageable pageable = PageRequest.of(page, size, sort);

        Long userId = getUserId();

        TransactionType transactionType = (type != null) ? TransactionType.valueOf(type.toUpperCase()) : null;
        CategoryType categoryType = (category != null) ? CategoryType.valueOf(category.toUpperCase()) : null;

        Page<Transaction> transactions = transactionService.getTransactions(
                userId, pageable, transactionType, categoryType
        );

        return ResponseEntity.ok(transactions);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTaskById(@PathVariable Long id) {
        Long userId = getUserId();
        try {
            transactionService.deleteTransaction(id, userId);
            return ResponseEntity.status(HttpStatus.OK).body("Task deleted successfully.");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task not found or access denied.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting task: " + e.getMessage());
        }
    }

    // общее количество транзакций пользователя
    @GetMapping("/total/{userId}")
    public Double getTotalAmountByUser(@PathVariable Long userId) {
        return transactionService.getTotalAmountByUser(userId);
    }

    // общее количество транзакций за период
    @GetMapping("/count")
    public Long getTransactionCountByDateRange(@RequestParam String startDate,
                                               @RequestParam String endDate) {
        return transactionService.getTransactionCountByDateRange(
                LocalDate.parse(startDate), LocalDate.parse(endDate));
    }

    @GetMapping("/category/{userId}")
    @Query("SELECT t.category, SUM(t.amount) FROM Transaction t WHERE t.user.id = :userId GROUP BY t.category")
    public List<Object[]> getTotalAmountByCategory(@PathVariable Long userId) {
        return transactionService.getTotalAmountByCategory(userId);
    }

    private Long getUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getPrincipal() instanceof CustomUserDetails userDetails) {
            return userDetails.getId();
        } else {
            throw new IllegalStateException("Authentication principal is not an instance of CustomUserDetails");
        }
    }
}