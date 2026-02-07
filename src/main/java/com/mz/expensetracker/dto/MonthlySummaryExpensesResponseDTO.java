package com.mz.expensetracker.dto;

import java.math.BigDecimal;
import java.util.List;

public class MonthlySummaryExpensesResponseDTO {

    private BigDecimal totalAmount;
    private String category;

    public MonthlySummaryExpensesResponseDTO(BigDecimal totalAmount, String category) {
        this.totalAmount = totalAmount;
        this.category = category;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public String getCategory() {
        return category;
    }
}
