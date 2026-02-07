package com.mz.expensetracker.service;


import com.mz.expensetracker.dto.ExpenseRequest;
import com.mz.expensetracker.dto.ExpenseResponse;
import com.mz.expensetracker.dto.MonthlySummaryExpensesResponseDTO;
import com.mz.expensetracker.model.Expense;
import com.mz.expensetracker.model.ExpenseCategory;
import com.mz.expensetracker.model.RecordStatus;
import com.mz.expensetracker.repository.ExpenseRepository;
import com.mz.userserviceauthentication.model.User;
import com.mz.userserviceauthentication.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Objects;

@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;

    public ExpenseService(ExpenseRepository expenseRepository, UserRepository userRepository) {
        this.expenseRepository = expenseRepository;
        this.userRepository = userRepository;
    }

   public ExpenseResponse addExpense(ExpenseRequest expenseRequest,String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Expense expense = new Expense(
                expenseRequest.getTitle(),
                expenseRequest.getAmount(),
                expenseRequest.getDescription(),
                expenseRequest.getCategory(),
                expenseRequest.getExpenseDate(),
                user
        );
        Expense savedExpense = expenseRepository.save(expense);

        return mapToResponse(savedExpense);
   }

   public Page<ExpenseResponse> getExpenses(
           String userEmail,
           String category,
           String startDate,
           String endDate,
           int page,
           int size) {

       User user = userRepository.findByEmail(userEmail)
               .orElseThrow(()->new RuntimeException("User not Found"));

       Pageable pageable = PageRequest.of(page, size,Sort.by("expenseDate").descending());

       Page<Expense> expenses;

       if(category!=null && !category.isBlank() && startDate!=null && endDate!=null) {
           ExpenseCategory expenseCategory;
           try {
               expenseCategory = ExpenseCategory.valueOf(category.toUpperCase());
           }catch (Exception e) {
               throw new RuntimeException("Invalid expense category:- "+category);
           }
           expenses = expenseRepository.findByUserAndCategoryAndExpenseDateBetween(
                   user,
                   expenseCategory,
                   LocalDate.parse(startDate),
                   LocalDate.parse(endDate),
                   pageable
           );
       }else {
           expenses = expenseRepository.findByUserAndStatus(user, RecordStatus.A, pageable);
       }
        return expenses.map(this::mapToResponse);
   }

    private ExpenseResponse mapToResponse(Expense expense) {
        return new ExpenseResponse(
                expense.getId(),
                expense.getTitle(),
                expense.getAmount(),
                expense.getCategory(),
                expense.getDescription(),
                expense.getExpenseDate()
        );
    }

    public ExpenseResponse updateExpense(Long id, @Valid ExpenseRequest expenseRequest, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(()->new RuntimeException("User not found"));
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(()->new RuntimeException("Expense not found"));

        if (!Objects.equals(expense.getUser().getId(),user.getId())) {
            throw new RuntimeException("User not authorized to update expense");
        }
        if(expense.getStatus().equals(RecordStatus.I)) {
            throw new RuntimeException("Expense is deleted");
        }
        else {
            expense.setTitle(expenseRequest.getTitle());
            expense.setAmount(expenseRequest.getAmount());
            expense.setDescription(expenseRequest.getDescription());
            expense.setCategory(expenseRequest.getCategory());
            expense.setExpenseDate(expenseRequest.getExpenseDate());
        }
        Expense updated = expenseRepository.save(expense);
        return mapToResponse(updated);
    }

    public void deleteExpense(Long id, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(()->new RuntimeException("User not found"));
        Expense expense = expenseRepository.findByIdAndStatus(id,RecordStatus.A)
                .orElseThrow(()->new RuntimeException("Expense not found"));

        if (!Objects.equals(expense.getUser().getId(),user.getId())) {
            throw new RuntimeException("User not authorized to delete expense");
        }
        expense.setStatus(RecordStatus.I);
        expenseRepository.save(expense);
    }

    public List<MonthlySummaryExpensesResponseDTO> getMonthlyExpenses(
            String email,
            int year,
            int month
    ){
        if(month < 1 || month >12){
            throw new IllegalArgumentException("Month out of range");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(()->new RuntimeException("User not found"));

        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate start = yearMonth.atDay(1);
        LocalDate end = yearMonth.atEndOfMonth();

        List<Object[]> rows = expenseRepository.findMonthlyCategorySummary(user, start, end);

        return rows.stream()
                .map(row-> new MonthlySummaryExpensesResponseDTO(
                        (BigDecimal) row[1],
                        row[0].toString()

                ))
                .toList();
    }

}
