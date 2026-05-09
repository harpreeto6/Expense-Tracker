package com.harpreet.expenseTracker.controller;

import com.harpreet.expenseTracker.model.Expense;
import com.harpreet.expenseTracker.service.ExpenseService;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.Valid;

import java.util.List;

@RestController
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }


    //a get mapping to return all the expenses in the database
    @GetMapping("/expenses")
    public List<Expense> getAllExpenses() {
        return expenseService.getAllExpenses();
    }

    @GetMapping("/hello")
    public String sayHello() {
        return "Hello, welcome to the Expense Tracker!";
    }

    @GetMapping("/hi")
    public String sayHi() {
        return expenseService.sayHi();
    }

    @GetMapping("/expenses/{id}")
    public Expense getExpenseById(@PathVariable Long id) {
        Expense ep =  expenseService.getExpenseById(id);
        if (ep == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return ep;
    }

    @GetMapping("/expenses?startDate={startDate}&endDate={endDate}")
    public ResponseEntity<List<Expense>> getExpensesByDateRange(@PathVariable String startDate, @PathVariable String endDate) {
        List<Expense> sol = expenseService.getExpensesByDateRange(startDate, endDate);
        return new ResponseEntity<>(sol, HttpStatus.OK);
    }

    @GetMapping("/expenses/category/{category}")
    public ResponseEntity<List<Expense>> getExpenseByCategory(@PathVariable String category) {
        List<Expense> expensesByCategory = expenseService.getExpensesByCategory(category);
        return new ResponseEntity<>(expensesByCategory, HttpStatus.OK);
    }

    @GetMapping("/expenses/total")
    public double calculateTotalExpenses() {
        return expenseService.calculateTotalExpenses();
    }

    @PostMapping("/expenses")
    public ResponseEntity<Expense> addExpense(@Valid @RequestBody Expense expense) {
        Expense ep = expenseService.addExpense(expense);
        return new ResponseEntity<>(ep, HttpStatus.CREATED);
    }

    @DeleteMapping("/expenses/{id}")
    public ResponseEntity<String> deleteExpenseById(@PathVariable Long id) {

        if (expenseService.deleteExpenseById(id)) {
            return new ResponseEntity<>("Expense with ID " + id + " deleted successfully.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Expense with ID " + id + " not found.", HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/expenses/{id}")
    public ResponseEntity<String> updateExpenseById(@PathVariable Long id,@Valid @RequestBody Expense updatedExpense) {
        if (expenseService.updateExpenseById(id, updatedExpense)) {
            return new ResponseEntity<>("Expense with ID " + id + " updated successfully.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Expense with ID " + id + " not found.", HttpStatus.NOT_FOUND);
        }
    }
}