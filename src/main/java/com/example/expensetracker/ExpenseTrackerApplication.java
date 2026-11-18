package com.example.expensetracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point for the Expense Tracker Spring Boot application.
 * 
 * `@SpringBootApplication` activates Spring Boot's autoconfiguration, component scan,
 * and external configuration features.
 */
@SpringBootApplication
public class ExpenseTrackerApplication {

	/**
	 * Main method used to run the application.
	 *
	 * @param args command-line arguments
	 */
	public static void main(String[] args) {
		// Launches the Spring environment and starts the embedded server
		SpringApplication.run(ExpenseTrackerApplication.class, args);
	}
}
