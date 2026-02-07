package com.mz.expensetracker.controller;


import com.mz.expensetracker.dto.ExpenseRequest;
import com.mz.expensetracker.dto.ExpenseResponse;
import com.mz.expensetracker.dto.MonthlySummaryExpensesResponseDTO;
import com.mz.expensetracker.service.ExpenseService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/expenses")
public class ExpensesController {

    private final ExpenseService expenseService;

    public ExpensesController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @PostMapping
    public ExpenseResponse addExpense(@Valid @RequestBody ExpenseRequest expenseRequest, Authentication authentication) {
        if (authentication == null || authentication.getName() == null) {
            throw new RuntimeException("Authentication name is null");
        }
        String email = authentication.getName();
        return expenseService.addExpense(expenseRequest, email);
    }

    @GetMapping
    public Page<ExpenseResponse> getExpenses(Authentication authentication,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        if (authentication == null || authentication.getName() == null) {
            throw new RuntimeException("Authentication name is null");
        }
        String email = authentication.getName();
        return expenseService.getExpenses(email,category,startDate,endDate, page, size);
    }

    @PutMapping("/{id}")
    public ExpenseResponse updateExpense(@PathVariable Long id,
                                         @Valid @RequestBody ExpenseRequest expenseRequest,
                                         Authentication authentication) {
        if (authentication == null || authentication.getName() == null) {
            throw new RuntimeException("Authentication name is null");
        }
        String email = authentication.getName();
        return expenseService.updateExpense(id,expenseRequest,email);
    }

    @DeleteMapping("/{id}")
    public void deleteExpense(@PathVariable Long id,Authentication authentication) {
        if (authentication == null || authentication.getName() == null) {
            throw new RuntimeException("Authentication name is null");
        }
        String email = authentication.getName();
        expenseService.deleteExpense(id,email);
    }

    @GetMapping("/summary/monthly")
    public List<MonthlySummaryExpensesResponseDTO> getMonthlySummaryExpenses(
            @RequestParam int year,
            @RequestParam int month,
            Authentication authentication) {
        if (authentication == null || authentication.getName() == null) {
            throw new RuntimeException("Authentication name is null");
        }
        String email = authentication.getName();
        return expenseService.getMonthlyExpenses(email,year,month);
    }
}
