package com.jonah.service.expense.impl;

import com.jonah.model.AppUser;
import com.jonah.model.Expense;
import com.jonah.repository.ExpenseRepository;
import com.jonah.repository.UserRepository;
import com.jonah.service.expense.ExpenseService;
import com.jonah.service.user.UserService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Profile("db")
public class ExpenseServiceImplDb implements ExpenseService {

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

        Optional<AppUser> optionalAppUser = userService.findUserById( userId );

        if( optionalAppUser.isPresent() ){
            AppUser user = optionalAppUser.get();
            expense.setUser( user );
            return expenseRepository.save( expense );
        }

        throw new RuntimeException("User not found");
    }

    @Override
    public boolean updateExpense(Expense updatedExpense, Long userId) {
        Optional<Expense> currentExpenseOpt = expenseRepository.findByIdAndUserId(
                    updatedExpense.getId(), userId)
                .stream()
                .filter(expense -> expense.getId().equals(updatedExpense.getId())
                ).findFirst();
        if( currentExpenseOpt.isPresent() ){

            updatedExpense.setUser( currentExpenseOpt.get().getUser() );
            expenseRepository.save( updatedExpense );
            return true;
        }

        return false;
    }

    @Override
    public boolean deleteExpense(Long id, Long userId) {
        Optional<Expense> currentExpenseOpt = expenseRepository.findByIdAndUserId( id , userId );

        if( currentExpenseOpt.isPresent() ){
            expenseRepository.deleteById( id );
            return  true;
        }
        return false;
    }
}
