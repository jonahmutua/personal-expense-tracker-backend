package com.jonah.service.expense;

import com.jonah.dto.ExpenseDto;
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

    public ExpenseDto addExpense(ExpenseDto expenseDto, Long userId);

    public Expense updateExpense(Expense expense, Long userId);

    public  void deleteExpense(Long id, Long userId);
}
