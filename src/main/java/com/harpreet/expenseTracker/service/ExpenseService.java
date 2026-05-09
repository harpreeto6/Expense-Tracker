package com.harpreet.expenseTracker.service;

import com.harpreet.expenseTracker.model.Expense;
import com.harpreet.expenseTracker.repository.ExpenseRepository;

import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import jakarta.validation.Valid;

import java.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

@Service
public class ExpenseService {

    //private final List<Expense> expenses = new ArrayList<>();
    //private Long nextId = 3L;
    private final ExpenseRepository expenseRepository;


    public ExpenseService(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
        //expenses.add(new Expense(1L, "Groceries", 42.50, "Food", LocalDate.of(2026, 4, 25)));
        //expenses.add(new Expense(2L, "Bus Pass", 25.00, "Transport", LocalDate.of(2026, 4, 26)));
    }

    public List<Expense> getAllExpenses() {
        return expenseRepository.findAll();
    }

    public String sayHi(){
        return "This is your Expense Service class saying hi!";
    }

    public Expense addExpense(Expense expense) {
        return expenseRepository.save(expense);
    }

    public double calculateTotalExpenses() {
        return expenseRepository.findAll().stream()
                .mapToDouble(Expense::getAmount)
                .sum();
    }

    public Expense getExpenseById(Long id) {
        return expenseRepository.findById(id).orElse(null);

    }

    public boolean deleteExpenseById(Long id) {
        if (expenseRepository.existsById(id)) {
            expenseRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<Expense> getExpensesByDateRange(String startDate, String endDate) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);

        List<Expense> allExpenses = expenseRepository.findAll();
        List<Expense> filteredExpenses = new ArrayList<>();
        for (Expense expense : allExpenses) {
            if (!expense.getDate().isBefore(start) && !expense.getDate().isAfter(end)) {
                filteredExpenses.add(expense);
            }
        }
        return filteredExpenses;
    }

    public List<Expense> getExpensesByCategory(String category) {

        List<Expense> allExpenses = expenseRepository.findAll();
        List<Expense> filteredExpenses = new ArrayList<>();
        for (Expense expense : allExpenses) {
            if (expense.getCategory().equalsIgnoreCase(category)) {
                filteredExpenses.add(expense);
            }
        }
        return filteredExpenses;
    }

    public boolean updateExpenseById(Long id, Expense updatedExpense) {
        if (expenseRepository.existsById(id)) {
            updatedExpense.setId(id);
            expenseRepository.save(updatedExpense);
            return true;
        }
        return false;
    }
}