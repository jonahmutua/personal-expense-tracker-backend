package com.jonah.mapper;

import com.jonah.dto.ExpenseDto;
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


    ExpenseDto toDto(Expense expense);


    Expense fromDto(ExpenseDto expenseDto);

}
