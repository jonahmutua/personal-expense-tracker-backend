package com.jonah.service.expense.impl;

import com.jonah.exception.ResourceNotFoundException;
import com.jonah.mapper.ExpenseMapper;
import com.jonah.model.AppUser;
import com.jonah.model.Expense;
import com.jonah.repository.ExpenseRepository;
import com.jonah.repository.UserRepository;
import com.jonah.service.expense.ExpenseService;
import com.jonah.service.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Profile("db")
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
    public List<Expense> getAllUserExpenses(Long userId) {

        return new ArrayList<>( expenseRepository.findByUserIdOrderByDate( userId ) );
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
    public List<Expense> getExpenseByCategoryAndMonth(String category, String month, Long userId) {

       return expenseRepository.findByUserIdOrderByDate( userId ).stream()
               .filter( expense -> expense.getCategory().equalsIgnoreCase( category )
                    && expense.getDate().startsWith( month ) )
               .toList();
    }

    @Override
    public Expense addExpense(Expense expense, Long userId) {
        AppUser user = userService.findUserById( userId )
                .orElseThrow( () -> new ResourceNotFoundException(
                        String.format("Failed to save expense: User with id=%d not found", userId), userId));

        // validate expense data & set expense user
        this.validateExpense( expense);
        expense.setUser( user );

        return expenseRepository.save( expense);

    }

    @Override
    public Expense updateExpense(Expense updatedExpense, Long userId) {
            Long expenseId = updatedExpense.getId();
            // Retrieve current expense or throw exception
            Expense currentExpense =  expenseRepository
                    .findByIdAndUserId(expenseId, userId)
                    .orElseThrow(()-> new ResourceNotFoundException(
                            String.format("Cannot update expense: Expense with id=%d for userId=%d not found",
                                    expenseId , userId), updatedExpense.getId()));

            // validate expense
            this.validateExpense( updatedExpense );

            // map and update
            this.expenseMapper.updateExpenseFromDto(updatedExpense, currentExpense);

            return expenseRepository.save(currentExpense);
    }

    @Override
    public void deleteExpense(Long id, Long userId) {
            // Retrieve expense or throw exception
            Expense expense = expenseRepository.findByIdAndUserId(id, userId)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            String.format("Cannot delete expense: Expense with id=%d for userId=%d not found", id, userId)
                            , id));

            // delete
            expenseRepository.delete( expense );
    }

    // Validates expense data
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
}
