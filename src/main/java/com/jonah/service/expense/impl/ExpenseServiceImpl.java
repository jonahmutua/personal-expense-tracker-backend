package com.jonah.service.expense.impl;

import com.jonah.dto.filter.ExpenseFilterDto;
import com.jonah.dto.request.ExpenseDto;
import com.jonah.model.Expense;
import com.jonah.service.expense.ExpenseService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Profile("json")
public class ExpenseServiceImpl implements ExpenseService {
    // TODO: implementation to support load/update from local json file ->> mimics  real DB
    @Override
    public List<ExpenseDto> getAllUserExpenses(Long userId) {
        return List.of();
    }

    @Override
    public List<String> getExpenseCategories(Long userId) {
        return List.of();
    }

    @Override
    public List<String> getExpenseCategoriesByMonth(String month, Long userId) {
        // ToDo: implementantion logic here
        return List.of();
    }

    @Override
    public Optional<Expense> getExpenseById(Long id, Long userId) {
        return Optional.empty();
    }

    @Override
    public List<Expense> getExpenseByDay(String date, Long userId) {
        return List.of();
    }

    @Override
    public List<ExpenseDto> getExpenseByCategoryAndMonth(String category, String month, Long userId) {
        return List.of();
    }

    @Override
    public ExpenseDto addExpense(ExpenseDto expenseDto, Long userId) {
        return null;
    }

    @Override
    public ExpenseDto updateExpense(ExpenseDto expense, Long userId) {
        return null;
    }

    @Override
    public List<ExpenseDto> filterExpenses(ExpenseFilterDto filterDto, Long userId) {
        return List.of();
    }

    @Override
    public void deleteExpense(Long id, Long userId) {
        return ;
    }


//
//    private AtomicLong idCounter = new AtomicLong();
//
//    @Override
//    public List<Expense> getAllExpenses() {
//        return ExpenseDataLoader.getExpenses();
//    }
//
//    @Override
//    public List<String> getExpenseCategories() {
//
//        return ExpenseDataLoader.getExpenses().stream().map(Expense::getCategory)
//                .distinct()
//                .toList();
//    }
//
//    @Override
//    public Optional<Expense> getExpenseById(Long id) {
//        return ExpenseDataLoader.getExpenses().stream().filter(
//                e-> e.getId().equals( id )
//        ).findFirst();
//    }
//
//    @Override
//    public List<Expense> getExpenseByDay(String date) {
//
//        return ExpenseDataLoader.getExpenses().stream().filter(
//                expense->expense.getDate().equalsIgnoreCase(date)
//        ).toList();
//    }
//
//    @Override
//    public List<Expense> getExpenseByCategoryAndMonth(String category, String month) {
//
//       return ExpenseDataLoader.getExpenses().stream().filter(
//               expense->expense.getCategory().equalsIgnoreCase(category)  &&
//                       expense.getDate().startsWith(month)
//       ).toList();
//    }
//
//    @Override
//    public Expense addExpense(Expense expense) {
//        expense.setId(idCounter.incrementAndGet() + ExpenseDataLoader.getBaseIdCounter());
//        ExpenseDataLoader.getExpenses().add( expense);
//        return expense;
//    }
//
//    @Override
//    public boolean updateExpense(Expense updatedExpense) {
//        Optional<Expense> currentExpenseOpt = ExpenseDataLoader.getExpenses().stream()
//                .filter(expense -> expense.getId().equals(updatedExpense.getId())
//                ).findFirst();
//        if( currentExpenseOpt.isPresent()){
//            Expense currentExpense = currentExpenseOpt.get();
//            currentExpense.setId(updatedExpense.getId());
//            currentExpense.setExpenseType(updatedExpense.getExpenseType());
//            currentExpense.setDate(updatedExpense.getDate());
//            currentExpense.setAmount(updatedExpense.getAmount());
//            currentExpense.setAccount(updatedExpense.getAccount());
//            currentExpense.setNote(updatedExpense.getNote());
//
//            return true;
//        }
//        return false;
//    }
//
//    @Override
//    public boolean deleteExpense(Long id) {
//        Optional<Expense> currentExpenseOpt = getExpenseById( id);
//
//        if(currentExpenseOpt.isPresent()){
//
//            ExpenseDataLoader.getExpenses().remove( currentExpenseOpt.get());
//            return  true;
//        }
//        return false;
//    }

}
