# Activity Diagram – Expense Tracker

> Shows the workflow / control flow for each major user interaction.

---

## 1. View Home Page (GET /)

```mermaid
flowchart TD
    A([Start]) --> B["User navigates to '/'"]
    B --> C["DispatcherServlet routes to\nExpenseController.viewHomePage()"]
    C --> D["Call expenseService.getAllExpense()"]
    D --> E["Call expenseRepository.findAll()"]
    E --> F[("H2 DB: SELECT * FROM expense")]
    F --> G["Return List of Expense objects"]
    G --> H["Stream expenses, sum all amounts\nusing BigDecimal::add"]
    H --> I["Add 'expenses' list to Model"]
    I --> J["Add 'totalAmount' to Model"]
    J --> K["Return view: 'index'"]
    K --> L["Thymeleaf renders index.html\nwith expenses table & total"]
    L --> M([End – User sees expense list])
```

---

## 2. Add New Expense

```mermaid
flowchart TD
    A([Start]) --> B["User clicks 'Add Expense'"]
    B --> C["GET /addExpense"]
    C --> D["Controller creates empty Expense object"]
    D --> E["Adds 'expense' to Model"]
    E --> F["Return view: 'add-expense'"]
    F --> G["User fills in Description & Amount"]
    G --> H{"User submits form?"}
    H -- No/Cancel --> Z([End – Return to home])
    H -- Yes --> I["POST /saveExpense"]
    I --> J["@ModelAttribute binds form data\nto Expense object"]
    J --> K["Call expenseService.saveExpense(expense)"]
    K --> L["Call expenseRepository.save(expense)"]
    L --> M[("H2 DB: INSERT INTO expense")]
    M --> N["Redirect to '/'"]
    N --> O([End – Expense added, home refreshed])
```

---

## 3. Edit / Update Expense

```mermaid
flowchart TD
    A([Start]) --> B["User clicks 'Edit' on an expense"]
    B --> C["GET /editExpense/{id}"]
    C --> D["Call expenseService.getExpenseById(id)"]
    D --> E[("H2 DB: SELECT * FROM expense WHERE id=?")]
    E --> F{"Expense found?"}
    F -- No --> G["Redirect to '/'"]
    G --> Z1([End – Not found, go home])
    F -- Yes --> H["Add existing expense to Model"]
    H --> I["Return view: 'update-expense'"]
    I --> J["User modifies Description and/or Amount"]
    J --> K["POST /updateExpense/{id}"]
    K --> L["Fetch existing expense by id again"]
    L --> M{"Expense still exists?"}
    M -- No --> N["Redirect to '/'"]
    N --> Z2([End – Deleted by another user])
    M -- Yes --> O["Apply new description & amount\nto existing entity"]
    O --> P["Call expenseService.saveExpense(existingExpense)"]
    P --> Q["Call expenseRepository.save(existingExpense)"]
    Q --> R[("H2 DB: UPDATE expense SET ... WHERE id=?")]
    R --> S["Redirect to '/'"]
    S --> T([End – Expense updated, home refreshed])
```

---

## 4. Delete Expense

```mermaid
flowchart TD
    A([Start]) --> B["User clicks 'Delete' on an expense"]
    B --> C["GET /deleteExpense/{id}"]
    C --> D["Call expenseService.deleteExpenseById(id)"]
    D --> E["Call expenseRepository.deleteById(id)"]
    E --> F[("H2 DB: DELETE FROM expense WHERE id=?")]
    F --> G["Redirect to '/'"]
    G --> H([End – Expense deleted, home refreshed])
```

---

## 5. Application Startup (Spring Boot Bootstrap)

```mermaid
flowchart TD
    A([Start]) --> B["JVM executes\nExpenseTrackerApplication.main()"]
    B --> C["SpringApplication.run() called"]
    C --> D["Spring IoC Container initialises"]
    D --> E["Component scan detects\n@Controller, @Service, @Repository, @Entity"]
    E --> F["Beans instantiated and\n@Autowired dependencies injected"]
    F --> G["Hibernate reads @Entity Expense\nand auto-creates/updates DB schema\n(ddl-auto=update)"]
    G --> H["H2 in-memory DB ready\njdbc:h2:mem:testdb"]
    H --> I["Embedded Tomcat server starts\non port 8080"]
    I --> J["Application ready to handle requests"]
    J --> K([End – System running])
```
