package com.mz.expensetracker.repository;

import com.mz.expensetracker.model.Expense;
import com.mz.expensetracker.model.ExpenseCategory;
import com.mz.userserviceauthentication.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    Page<Expense> findByUser(User user, Pageable pageable);

    Page<Expense> findByUserAndCategoryAndExpenseDateBetween(
            User user, ExpenseCategory category, LocalDate startDate, LocalDate endDate, Pageable pageable);
}
