# Sequence Diagram – Expense Tracker

> Shows message exchanges between objects over time for each major operation.

---

## 1. View All Expenses (GET /)

```mermaid
sequenceDiagram
    actor User
    participant Browser
    participant DispatcherServlet as DispatcherServlet<br/>(Spring MVC)
    participant Controller as ExpenseController
    participant Service as ExpenseService
    participant Repository as ExpenseRepository
    participant DB as H2 Database

    User->>Browser: Navigate to http://localhost:8080/
    Browser->>DispatcherServlet: HTTP GET /
    DispatcherServlet->>Controller: viewHomePage(model)
    Controller->>Service: getAllExpense()
    Service->>Repository: findAll()
    Repository->>DB: SELECT * FROM expense
    DB-->>Repository: ResultSet (rows)
    Repository-->>Service: List<Expense>
    Service-->>Controller: List<Expense>
    Controller->>Controller: Stream + reduce to calculate totalAmount
    Controller->>Controller: model.addAttribute("expenses", expenses)
    Controller->>Controller: model.addAttribute("totalAmount", totalAmount)
    Controller-->>DispatcherServlet: return "index"
    DispatcherServlet->>Browser: HTTP 200 – rendered index.html
    Browser-->>User: Displays expense list with total
```

---

## 2. Add New Expense

```mermaid
sequenceDiagram
    actor User
    participant Browser
    participant DispatcherServlet as DispatcherServlet<br/>(Spring MVC)
    participant Controller as ExpenseController
    participant Service as ExpenseService
    participant Repository as ExpenseRepository
    participant DB as H2 Database

    User->>Browser: Click "Add Expense"
    Browser->>DispatcherServlet: HTTP GET /addExpense
    DispatcherServlet->>Controller: showAddExpensePage(model)
    Controller->>Controller: model.addAttribute("expense", new Expense())
    Controller-->>DispatcherServlet: return "add-expense"
    DispatcherServlet->>Browser: HTTP 200 – rendered add-expense.html
    Browser-->>User: Display empty expense form

    User->>Browser: Fill description + amount, submit form
    Browser->>DispatcherServlet: HTTP POST /saveExpense
    DispatcherServlet->>Controller: saveExpense(@ModelAttribute expense)
    Controller->>Service: saveExpense(expense)
    Service->>Repository: save(expense)
    Repository->>DB: INSERT INTO expense (description, amount) VALUES (?, ?)
    DB-->>Repository: Generated id returned
    Repository-->>Service: Saved Expense entity
    Service-->>Controller: (void)
    Controller-->>DispatcherServlet: return "redirect:/"
    DispatcherServlet->>Browser: HTTP 302 Redirect → /
    Browser->>DispatcherServlet: HTTP GET /
    DispatcherServlet->>Browser: HTTP 200 – updated home page
    Browser-->>User: Home page with new expense listed
```

---

## 3. Edit / Update Expense

```mermaid
sequenceDiagram
    actor User
    participant Browser
    participant DispatcherServlet as DispatcherServlet<br/>(Spring MVC)
    participant Controller as ExpenseController
    participant Service as ExpenseService
    participant Repository as ExpenseRepository
    participant DB as H2 Database

    User->>Browser: Click "Edit" on expense (id=2)
    Browser->>DispatcherServlet: HTTP GET /editExpense/2
    DispatcherServlet->>Controller: showUpdateExpensePage(id=2, model)
    Controller->>Service: getExpenseById(2)
    Service->>Repository: findById(2)
    Repository->>DB: SELECT * FROM expense WHERE id=2
    DB-->>Repository: Row data
    Repository-->>Service: Optional<Expense> (present)
    Service-->>Controller: Expense object
    Controller->>Controller: model.addAttribute("expense", expense)
    Controller-->>DispatcherServlet: return "update-expense"
    DispatcherServlet->>Browser: HTTP 200 – rendered update-expense.html
    Browser-->>User: Pre-filled edit form

    User->>Browser: Modify fields, submit form
    Browser->>DispatcherServlet: HTTP POST /updateExpense/2
    DispatcherServlet->>Controller: updateExpense(id=2, @ModelAttribute expense)
    Controller->>Service: getExpenseById(2)
    Service->>Repository: findById(2)
    Repository->>DB: SELECT * FROM expense WHERE id=2
    DB-->>Repository: Row data
    Repository-->>Service: Optional<Expense>
    Service-->>Controller: Existing Expense
    Controller->>Controller: existingExpense.setDescription(...)
    Controller->>Controller: existingExpense.setAmount(...)
    Controller->>Service: saveExpense(existingExpense)
    Service->>Repository: save(existingExpense)
    Repository->>DB: UPDATE expense SET description=?, amount=? WHERE id=2
    DB-->>Repository: OK
    Repository-->>Service: Updated Expense
    Service-->>Controller: (void)
    Controller-->>DispatcherServlet: return "redirect:/"
    DispatcherServlet->>Browser: HTTP 302 Redirect → /
    Browser-->>User: Refreshed home with updated expense
```

---

## 4. Delete Expense

```mermaid
sequenceDiagram
    actor User
    participant Browser
    participant DispatcherServlet as DispatcherServlet<br/>(Spring MVC)
    participant Controller as ExpenseController
    participant Service as ExpenseService
    participant Repository as ExpenseRepository
    participant DB as H2 Database

    User->>Browser: Click "Delete" on expense (id=3)
    Browser->>DispatcherServlet: HTTP GET /deleteExpense/3
    DispatcherServlet->>Controller: deleteExpense(id=3)
    Controller->>Service: deleteExpenseById(3)
    Service->>Repository: deleteById(3)
    Repository->>DB: DELETE FROM expense WHERE id=3
    DB-->>Repository: Rows affected: 1
    Repository-->>Service: (void)
    Service-->>Controller: (void)
    Controller-->>DispatcherServlet: return "redirect:/"
    DispatcherServlet->>Browser: HTTP 302 Redirect → /
    Browser->>DispatcherServlet: HTTP GET /
    DispatcherServlet->>Browser: HTTP 200 – updated home page
    Browser-->>User: Home page without deleted expense
```

---

## 5. Application Startup Sequence

```mermaid
sequenceDiagram
    participant JVM
    participant App as ExpenseTrackerApplication
    participant Spring as Spring IoC Container
    participant Hibernate as Hibernate / JPA
    participant DB as H2 Database
    participant Tomcat as Embedded Tomcat

    JVM->>App: main(args)
    App->>Spring: SpringApplication.run(ExpenseTrackerApplication.class, args)
    Spring->>Spring: Component scan – detect @SpringBootApplication
    Spring->>Spring: Instantiate ExpenseRepository bean
    Spring->>Spring: Instantiate ExpenseService bean
    Spring->>Spring: @Autowired ExpenseRepository → ExpenseService
    Spring->>Spring: Instantiate ExpenseController bean
    Spring->>Spring: @Autowired ExpenseService → ExpenseController
    Spring->>Hibernate: Configure DataSource (H2, sa/password)
    Hibernate->>DB: Connect to jdbc:h2:mem:testdb
    DB-->>Hibernate: Connection established
    Hibernate->>DB: CREATE/ALTER TABLE expense (ddl-auto=update)
    DB-->>Hibernate: Schema ready
    Hibernate-->>Spring: JPA context ready
    Spring->>Tomcat: Start embedded server on port 8080
    Tomcat-->>Spring: Server started
    Spring-->>App: Application context ready
    App-->>JVM: Startup complete – listening on :8080
```
