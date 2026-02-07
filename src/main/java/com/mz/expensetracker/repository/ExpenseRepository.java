package com.mz.expensetracker.repository;

import com.mz.expensetracker.model.Expense;
import com.mz.expensetracker.model.ExpenseCategory;
import com.mz.expensetracker.model.RecordStatus;
import com.mz.userserviceauthentication.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    Page<Expense> findByUserAndStatus(User user, RecordStatus status, Pageable pageable);
    Optional<Expense> findByIdAndStatus(Long id, RecordStatus status);
    Page<Expense> findByUserAndCategoryAndExpenseDateBetween(
            User user, ExpenseCategory category, LocalDate startDate, LocalDate endDate, Pageable pageable);
}
