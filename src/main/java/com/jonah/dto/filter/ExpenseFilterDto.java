package com.jonah.dto.filter;


import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseFilterDto {

    @Pattern(regexp = "\\d{4}-\\d{2}",
            message = "Month format must be yyyy-MM",
            groups = ValidationGroup.Month.class)
    String month;

    @NotBlank(message = "Category cannot be blank")
    String category;

    @Min(value = 0, message = "Minimum amount must be >= 0")
    private Double minAmount;

    @Max(value = 999999, message = "Max amount must be <= 999999")
    private Double maxAmount;

    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}",
            message = "Start date must be yyyy-MM-dd",
            groups = ValidationGroup.DateRange.class)
    private String startDate;

    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}",
            message = "End date must be yyyy-MM-dd",
            groups = ValidationGroup.DateRange.class)
    private String endDate;

    @NotBlank(message = "Account is required ")
    private String account;

    private String sortBy; // "date", "amount"

    private String sortOrder; // "asc", "desc"

    // checks if any filter is provided
    public boolean hasfilters() {
        return month != null || category != null || minAmount != null ||
                maxAmount != null || startDate != null || endDate != null ||
                account != null;
    }

}
