package com.mz.expensetracker.repository;

import com.mz.expensetracker.model.Expense;
import com.mz.expensetracker.model.ExpenseCategory;
import com.mz.expensetracker.model.RecordStatus;
import com.mz.userserviceauthentication.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    Page<Expense> findByUserAndStatus(User user, RecordStatus status, Pageable pageable);
    Optional<Expense> findByIdAndStatus(Long id, RecordStatus status);
    Page<Expense> findByUserAndCategoryAndExpenseDateBetween(
            User user, ExpenseCategory category, LocalDate startDate, LocalDate endDate, Pageable pageable);

    @Query("""
    select e.category, sum(e.amount)
    from Expense e
    where 
    e.user = :user
    and e.status = com.mz.expensetracker.model.RecordStatus.A
    and e.expenseDate between :start and :end
    group by e.category
""")
    List<Object[]> findMonthlyCategorySummary(
            @Param("user") User user
    , @Param("start") LocalDate start
    , @Param("end") LocalDate end);

    @Query("""
    select coalesce(sum(e.amount),0) 
    from Expense e
    where e.user=:user
    and e.expenseDate between :start and :end
""")
    BigDecimal findMonthlyTotalAmount(
            @Param("user") User user,
            @Param("start") LocalDate start,
            @Param("end") LocalDate end
    );
}
