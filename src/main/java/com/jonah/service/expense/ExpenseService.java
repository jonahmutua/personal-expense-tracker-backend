package com.jonah.service.expense;

import com.jonah.model.Expense;

import java.util.List;
import java.util.Optional;

public interface ExpenseService {

    public List<Expense> getAllUserExpenses(Long userId);

    public List<String> getExpenseCategories(Long userId);

    public  List<String> getExpenseCategoriesByMonth(String month , Long userId);

    public Optional<Expense> getExpenseById(Long id, Long userId);

    public List<Expense> getExpenseByDay(String date, Long userId);

    public List<Expense> getExpenseByCategoryAndMonth(String category, String month, Long userId);

    public Expense addExpense(Expense expense, Long userId);

    public boolean updateExpense(Expense expense, Long userId);

    public  boolean deleteExpense(Long id, Long userId);
}
