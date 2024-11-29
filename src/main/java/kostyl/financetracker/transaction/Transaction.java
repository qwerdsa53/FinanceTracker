package kostyl.financetracker.transaction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import kostyl.financetracker.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CategoryType category;

    @Column
    private String description;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionType type;
}
