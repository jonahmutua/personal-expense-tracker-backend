package com.jonah.dto.request;

import com.jonah.model.ExpenseType;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

@Data
public class ExpenseDto {

    private Long id;

    // ToDo: Add Consraint
    private ExpenseType expenseType;

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
