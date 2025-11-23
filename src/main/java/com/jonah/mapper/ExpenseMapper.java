package com.jonah.mapper;

import com.jonah.model.Expense;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface ExpenseMapper {

    // updates expense from dto - only provided fields are mapped, null fields will be ignored
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "id", ignore = true)
    void updateExpenseFromDto(Expense source,@MappingTarget Expense destination);

    // Covert expense to DTO
    //ExpenseDto toDto(Expense expense); // TODO: create ExpenseDto

    // Convert ExpenseDto to entity
    //Expense fromDto(ExpenseDto expenseDto); // TODO: Create ExpenseDto

}
