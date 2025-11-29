package com.jonah.service.expense.impl;

import com.jonah.aspect.annotation.Log;
import com.jonah.dto.filter.ExpenseFilterDto;
import com.jonah.dto.request.ExpenseDto;
import com.jonah.exception.ResourceNotFoundException;
import com.jonah.mapper.ExpenseMapper;
import com.jonah.model.AppUser;
import com.jonah.model.Expense;
import com.jonah.repository.ExpenseRepository;
import com.jonah.service.expense.ExpenseService;
import com.jonah.service.user.UserService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Profile("db")
@Log
public class ExpenseServiceImplDb implements ExpenseService {

    @Autowired
    private  ExpenseMapper expenseMapper;

    private final ExpenseRepository expenseRepository;
    private final UserService userService;

    public ExpenseServiceImplDb(ExpenseRepository expenseRepository, UserService userService) {
        this.expenseRepository = expenseRepository;
        this.userService = userService;
    }

    @Override
    public List<ExpenseDto> getAllUserExpenses(Long userId) {
        return  expenseMapper.toListDto( expenseRepository.findByUserIdOrderByDate( userId ) );
    }

    @Override
    public List<String> getExpenseCategories(Long userId) {

        return expenseRepository.findByUserIdOrderByDate( userId )
                .stream()
                .map(Expense::getCategory)
                .distinct()
                .toList();
    }

    @Override
    public List<String> getExpenseCategoriesByMonth(String month, Long userId) {
        return expenseRepository.findByUserIdOrderByDate( userId )
                .stream()
                .filter(expense-> expense.getDate().startsWith( month) )
                .map(Expense::getCategory)
                .distinct()
                .toList();
    }

    @Override
    public Optional<Expense> getExpenseById(Long id, Long userId) {

        return expenseRepository.findByIdAndUserId( id, userId )
                .stream().filter(
                expense->expense.getId().equals( id ) )
                .findFirst();
    }

    @Override
    public List<Expense> getExpenseByDay(String date, Long userId) {
        return expenseRepository.findByUserIdOrderByDate( userId ).stream()
                .filter( expense -> expense.getDate().equals(date))
                .toList();
    }

    @Override
    public List<ExpenseDto> getExpenseByCategoryAndMonth(String category, String month, Long userId) {

         List<Expense> expenses = expenseRepository.findByCategoryAndMonthAndUserId(
                category, month, userId);

         if( expenses.isEmpty()) {
             //throw new ExpenseNotFoundException("No expenses found for  category:  " + category + " and month: " + month );
             throw new ResourceNotFoundException("No expense(s) that match criteria are found for user id: " + userId);
         }

         return expenseMapper.toListDto( expenses );

    }

    @Override
    public ExpenseDto addExpense(ExpenseDto expenseDto, Long userId) {
        AppUser user = userService.findUserById( userId )
                .orElseThrow( () -> new ResourceNotFoundException("User not found with id: " + userId));

        Expense expense = expenseMapper.fromDto( expenseDto);
        //this.validateExpense( expense); ToDO: remove - validation is handled in ExpenseDto class
        expense.setUser( user );
        return expenseMapper.toDto(expenseRepository.save( expense) );

    }

    @Override
    public ExpenseDto updateExpense(ExpenseDto updatedExpense, Long userId) {
            Long expenseId = updatedExpense.getId();
            Expense currentExpense =  expenseRepository
                    .findByIdAndUserId(expenseId, userId)
                    .orElseThrow(()-> new ResourceNotFoundException("Expense not found with id: " + expenseId));

            //this.validateExpense( updatedExpense );

            this.expenseMapper.updateExpenseFromDto(updatedExpense, currentExpense);

            return expenseMapper.toDto(expenseRepository.save(currentExpense));
    }
    @Override
    public List<ExpenseDto> filterExpenses(ExpenseFilterDto filterDto, Long userId) {

        List<Expense> expenses = expenseRepository.findByUserIdOrderByDate( userId );

        // apply filters
        List<Expense> filtered = expenses.stream()
                .filter(expense -> filterByMonth(expense, filterDto.getMonth()) )
                .filter( expense -> filterByCategory(expense, filterDto.getCategory()))
                .filter(expense -> filterByAmount(expense, filterDto.getMinAmount(), filterDto.getMaxAmount()))
                .filter(expense -> filterByDateRange(expense, filterDto.getStartDate(), filterDto.getEndDate()))
                .filter(expense -> filterByAccount(expense, filterDto.getAccount()))
                .toList(); // ToDo: explore Collectors...
        // sort
        filtered = sortExpenses(filtered, filterDto.getSortBy(), filterDto.getSortOrder() );

        return expenseMapper.toListDto( filtered ) ;
    }

    @Override
    public void deleteExpense(Long id, Long userId) {
            Expense expense = expenseRepository.findByIdAndUserId(id, userId)
                    .orElseThrow(() -> new ResourceNotFoundException("Expense not found with id: " + id));

            expenseRepository.delete( expense );
    }

    // private methods
    private void validateExpense(Expense expense){
        if(expense.getAmount() == null || expense.getAmount() < 0 ){
            throw new IllegalArgumentException("Expense amount must be grater than 0");
        }
        if(expense.getCategory() == null || expense.getCategory().isEmpty() ){
            throw new IllegalArgumentException("Expense category is required");
        }
        if(expense.getDate() == null || expense.getDate().isEmpty() ){
            throw new IllegalArgumentException("Expense date is required");
        }
    }

    private List<Expense> sortExpenses(List<Expense> expenses, String sortBy, String sortOrder) {
        if( sortBy == null || expenses == null || expenses.isEmpty()) {
            return  expenses;
        }

        boolean ascending = !"desc".equalsIgnoreCase( sortOrder);

        // Avoid null pointer exception if expense fields are null
        Comparator<Expense> comparator = switch (sortBy.toLowerCase()){
            case "amount" -> Comparator.comparing(Expense::getAmount, Comparator.nullsLast(Double::compare));
            case "category" -> Comparator.comparing(Expense::getCategory, Comparator.nullsLast(String::compareTo));
            default -> Comparator.comparing(Expense::getDate, Comparator.nullsLast(String::compareTo));
        };

        if(!ascending) {
            comparator = comparator.reversed();
        }
        return expenses.stream()
                .sorted(comparator)
                .toList();
    }

    private boolean filterByAccount(Expense expense, @NotBlank(message = "Account is required ") String account) {
        return account == null || expense.getAccount() != null && expense.getAccount().equalsIgnoreCase(account);
    }

    private boolean filterByDateRange(Expense expense, String startDate, String endDate) {
        if( startDate !=null && expense.getDate() != null && expense.getDate().compareTo(startDate) < 0 ) {
            return  false;
        }
        if( endDate != null && expense.getDate() != null && expense.getDate().compareTo(endDate) > 0 ) {
            return false;
        }
        return true;
    }

    private boolean filterByAmount(Expense expense, Double minAmount, Double maxAmount) {
        if(minAmount != null && expense.getAmount() < minAmount){
            return  false;
        }
        if( maxAmount != null && expense.getAmount() > maxAmount){
            return false;
        }
        return true;
    }

    private boolean filterByCategory(Expense expense, String category) {
        return category == null || expense.getCategory() != null && expense.getCategory().equalsIgnoreCase( category);
    }

    private boolean filterByMonth(Expense expense, String month) {
        return month == null || expense.getDate() != null && expense.getDate().startsWith( month);
    }

}
