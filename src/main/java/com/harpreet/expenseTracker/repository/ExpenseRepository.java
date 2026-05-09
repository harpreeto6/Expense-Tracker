package com.harpreet.expenseTracker.repository;

import com.harpreet.expenseTracker.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    // No additional methods needed for basic CRUD operations
}
