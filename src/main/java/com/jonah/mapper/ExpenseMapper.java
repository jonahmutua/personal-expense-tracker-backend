package com.jonah.mapper;

import com.jonah.dto.request.ExpenseDto;
import com.jonah.model.Expense;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface ExpenseMapper {

    // updates expense from dto - only provided fields are mapped, null fields will be ignored
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "id", ignore = true)
    void updateExpenseFromDto(ExpenseDto source,@MappingTarget Expense destination);


    ExpenseDto toDto(Expense expense);


    Expense fromDto(ExpenseDto expenseDto);

    List<ExpenseDto> toListDto(List<Expense> expense);

}
