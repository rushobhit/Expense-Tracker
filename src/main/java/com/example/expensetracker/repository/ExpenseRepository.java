package com.example.expensetracker.repository;

import com.example.expensetracker.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Data Access Object (DAO) interface for the Expense entity.
 * 
 * `@Repository` indicates that the class provides the mechanism for storage, retrieval, 
 * search, update and delete operation on objects. It also translates database exceptions 
 * into Spring's DataAccessException hierarchy.
 * By extending `JpaRepository`, this interface automatically inherits standard CRUD methods 
 * (like save, findById, findAll, deleteById) and pagination features for the Expense model 
 * targeting a Long primary key, without requiring any boilerplate implementations.
 */
@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {

}
