package com.harpreet.expenseTracker.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.MediaType;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.harpreet.expenseTracker.model.Expense;
import com.harpreet.expenseTracker.service.ExpenseService;

import tools.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(ExpenseController.class)
public class ExpenseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ExpenseService expenseService;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    void getAllExpenses_returns200AndList() throws Exception {
        List<Expense> expenses = List.of(
                new Expense(1L, "Lunch", 12.99, "Food", LocalDate.of(2026, 5, 1)),
                new Expense(2L, "Bus Pass", 25.00, "Transport", LocalDate.of(2026, 5, 2))
        );

        when(expenseService.getAllExpenses()).thenReturn(expenses);

        mockMvc.perform(get("/expenses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Lunch"))
                .andExpect(jsonPath("$[1].category").value("Transport"));
    }

    @Test
    void addExpense_validExpense_returns201() throws Exception {
        Expense inputExpense = new Expense(null, "Groceries", 42.50, "Food", LocalDate.of(2026, 5, 3));
        Expense savedExpense = new Expense(1L, "Groceries", 42.50, "Food", LocalDate.of(2026, 5, 3));

        when(expenseService.addExpense(any(Expense.class))).thenReturn(savedExpense);

        mockMvc.perform(post("/expenses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputExpense)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Groceries"));
    }

    @Test
    void addExpense_invalidExpense_returns400() throws Exception {
        String badJson = """
                {
                  "title": "",
                  "amount": 0,
                  "category": "",
                  "date": null
                }
                """;

        mockMvc.perform(post("/expenses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(badJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getExpenseById_notFound_returns404() throws Exception {
        when(expenseService.getExpenseById(99L)).thenReturn(null);

        mockMvc.perform(get("/expenses/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteExpense_notFound_returns404() throws Exception {

        String id = """
                99
                """;
        //when(expenseService.deleteExpense(id)).thenReturn(false);

        mockMvc.perform(delete("/expenses/99"))
                .andExpect(status().isNotFound());
    }

    
}
