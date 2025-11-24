package com.jonah.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

@Data
public class ExpenseDto {

    private Long id;

    @Range(min = 0, max = 1, message = "Expense Type must be 0 or 1")
    private int expenseType;

    @NotBlank(message = "Date is required")
    private String date;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be grater than 0")
    private Double amount;

    @NotNull(message = "Category is required")
    private String category;

    @NotBlank(message = "Account is required")
    private String account;

    @Size(max = 255, message = "Note cannot exceed 255 characters")
    private String note;

}
