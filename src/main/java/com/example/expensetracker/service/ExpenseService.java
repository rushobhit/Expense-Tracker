package com.example.expensetracker.service;

import com.example.expensetracker.model.Expense;
import com.example.expensetracker.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class handling all business logic operations related to Expenses.
 * 
 * `@Service` marks this class as a Spring Service component, specializing in containing
 * business rule definitions and acting as an intermediate layer between controllers and data access.
 */
@Service
public class ExpenseService {

    /**
     * Dependency injection of the ExpenseRepository bean to interact with the database.
     */
    @Autowired
    private ExpenseRepository expenseRepository;

    /**
     * Retrieves all expenses stored in the database.
     *
     * @return a list of all Expense objects
     */
    public List<Expense> getAllExpense() {
        // Calls the JpaRepository's findAll() to fetch all records from the expense table
        return expenseRepository.findAll();
    }

    /**
     * Saves a new expense or updates an existing expense in the database.
     *
     * @param expense the Expense entity to persist
     */
    public void saveExpense(Expense expense) {
        // Calls JpaRepository's save() which performs either an insert or update operation
        expenseRepository.save(expense);
    }

    /**
     * Retrieves a single expense by its unique ID.
     * If the expense is not found, returns null.
     *
     * @param id the unique ID of the expense to retrieve
     * @return the Expense entity if found, otherwise null
     */
    public Expense getExpenseById(Long id) {
        // findById returns an Optional. orElse(null) safely retrieves the value or returns null if empty.
        return expenseRepository.findById(id).orElse(null);
    }

    /**
     * Deletes an expense from the database by its unique ID.
     *
     * @param id the unique ID of the expense to delete
     */
    public void deleteExpenseById(Long id) {
        // Calls JpaRepository's deleteById() to remove the record by its primary key
        expenseRepository.deleteById(id);
    }
}
