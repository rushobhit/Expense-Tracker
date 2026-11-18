package com.example.expensetracker.controller;

import com.example.expensetracker.model.Expense;
import com.example.expensetracker.service.ExpenseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.math.BigDecimal;
import java.util.List;

/**
 * Controller class for managing expense tracking views and operations.
 * 
 * `@Controller` is a Spring Stereotype annotation indicating that the annotated class 
 * serves the role of a Controller in the Spring MVC flow. Unlike `@RestController`, methods in 
 * `@Controller` typically return view template names (e.g. HTML pages dynamically resolved by Thymeleaf).
 */
@Controller
public class ExpenseController {

    /**
     * The business logic service for handling expenses.
     * 
     * `@Autowired` marks this dependency to be automatically injected by Spring's 
     * Dependency Injection framework.
     */
    @Autowired
    private ExpenseService expenseService;

    /**
     * Handles HTTP GET requests for the root page (Home page).
     * Retrieves all expenses and calculates their total sum, mapping them to the model.
     * 
     * `@GetMapping("/")` maps GET requests to the root path.
     * `Model` is a container provided by Spring MVC to pass data/attributes to the view rendering engine.
     *
     * @param model the Spring MVC model
     * @return the view template name "index"
     */
    @GetMapping("/")
    public String viewHomePage(Model model) {
        // Fetch all expenses using the service
        List<Expense> expenses = expenseService.getAllExpense();

        // Calculate the total amount of all expenses using Java Streams API
        BigDecimal totalAmount = expenses.stream()
                .map(Expense::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Add variables to the model so they are accessible in the UI template (e.g., Thymeleaf)
        model.addAttribute("expenses", expenses);
        model.addAttribute("totalAmount", totalAmount);

        // Returns index.html to render the home view
        return "index";
    }

    /**
     * Handles HTTP GET requests to show the add expense form.
     * Places a blank Expense object into the model to bind form data.
     * 
     * `@GetMapping("/addExpense")` maps GET requests to show the form.
     *
     * @param model the Spring MVC model
     * @return the view template name "add-expense"
     */
    @GetMapping("/addExpense")
    public String showAddExpensePage(Model model) {
        // Prepare an empty Expense object to bind the form fields
        model.addAttribute("expense", new Expense());
        return "add-expense";
    }

    /**
     * Handles HTTP POST requests to save a new expense.
     * 
     * `@PostMapping("/saveExpense")` maps HTTP POST requests to save the submitted form data.
     * `@ModelAttribute` binds the form inputs directly to the properties of the Expense object.
     *
     * @param expense the Expense model bound with submitted form data
     * @return a redirect path to the home page
     */
    @PostMapping("/saveExpense")
    public String saveExpense(@ModelAttribute("expense") Expense expense) {
        // Persist the new expense via the service
        expenseService.saveExpense(expense);

        // Redirect user back to the home page route to refresh and see the updated list
        return "redirect:/";
    }

    /**
     * Handles HTTP GET requests to display the edit expense page.
     * Fetches the expense by its ID and adds it to the model.
     * 
     * `@GetMapping("/editExpense/{id}")` defines a dynamic URI path parameter `{id}`.
     * `@PathVariable` binds the dynamic path variable to the method argument.
     *
     * @param id the unique ID of the expense to edit
     * @param model the Spring MVC model
     * @return the view template name "update-expense" or a redirect to root if not found
     */
    @GetMapping("/editExpense/{id}")
    public String showUpdateExpensePage(@PathVariable("id") Long id, Model model) {
        // Fetch the existing expense by its unique ID
        Expense expense = expenseService.getExpenseById(id);

        // If no matching expense is found, gracefully redirect back to the home page
        if (expense == null) {
            return "redirect:/";
        }

        // Add the retrieved expense to the model to populate the update form
        model.addAttribute("expense", expense);
        return "update-expense";
    }

    /**
     * Handles HTTP POST requests to update an existing expense.
     * 
     * `@PostMapping("/updateExpense/{id}")` handles the form submission for updating an expense.
     *
     * @param id the unique ID of the expense to be updated
     * @param expense the updated expense data from the form
     * @return redirect path to the home page
     */
    @PostMapping("/updateExpense/{id}")
    public String updateExpense(@PathVariable Long id,
                                @ModelAttribute("expense") Expense expense) {
        // Retrieve the current details of the expense from the database
        Expense existingExpense = expenseService.getExpenseById(id);

        // If not found, return to home
        if (existingExpense == null) {
            return "redirect:/";
        }

        // Apply the updated values (description and amount) to the existing entity
        existingExpense.setDescription(expense.getDescription());
        existingExpense.setAmount(expense.getAmount());

        // Save changes back to the database
        expenseService.saveExpense(existingExpense);

        return "redirect:/";
    }

    /**
     * Handles HTTP GET requests to delete an expense by its ID.
     * 
     * `@GetMapping("/deleteExpense/{id}")` maps the delete endpoint.
     *
     * @param id the unique ID of the expense to delete
     * @return redirect path to the home page
     */
    @GetMapping("/deleteExpense/{id}")
    public String deleteExpense(@PathVariable Long id) {
        // Perform deletion via service
        expenseService.deleteExpenseById(id);

        return "redirect:/";
    }
}