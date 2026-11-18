# Class Diagram – Expense Tracker

> Shows all classes, their attributes, methods, relationships, and Spring stereotypes.

```mermaid
classDiagram
    direction TB

    class ExpenseTrackerApplication {
        +main(args : String[]) void
    }

    class Expense {
        -id : Long
        -description : String
        -amount : BigDecimal
        +getId() Long
        +setId(id : Long) void
        +getDescription() String
        +setDescription(description : String) void
        +getAmount() BigDecimal
        +setAmount(amount : BigDecimal) void
    }

    class ExpenseRepository {
        <<interface>>
        +findAll() List~Expense~
        +findById(id : Long) Optional~Expense~
        +save(expense : Expense) Expense
        +deleteById(id : Long) void
    }

    class JpaRepository {
        <<interface>>
        +findAll() List~T~
        +findById(id : ID) Optional~T~
        +save(entity : T) T
        +deleteById(id : ID) void
        +count() long
    }

    class ExpenseService {
        <<Service>>
        -expenseRepository : ExpenseRepository
        +getAllExpense() List~Expense~
        +saveExpense(expense : Expense) void
        +getExpenseById(id : Long) Expense
        +deleteExpenseById(id : Long) void
    }

    class ExpenseController {
        <<Controller>>
        -expenseService : ExpenseService
        +viewHomePage(model : Model) String
        +showAddExpensePage(model : Model) String
        +saveExpense(expense : Expense) String
        +showUpdateExpensePage(id : Long, model : Model) String
        +updateExpense(id : Long, expense : Expense) String
        +deleteExpense(id : Long) String
    }

    class H2Database {
        <<Database>>
        +expense : TABLE
    }

    %% Relationships
    ExpenseRepository --|> JpaRepository : extends
    ExpenseService --> ExpenseRepository : uses (Autowired)
    ExpenseController --> ExpenseService : uses (Autowired)
    ExpenseRepository --> Expense : manages
    ExpenseService --> Expense : processes
    ExpenseController --> Expense : binds (ModelAttribute)
    ExpenseRepository ..> H2Database : persists to
    ExpenseTrackerApplication ..> ExpenseController : bootstraps
```

## Notes

| Class | Spring Annotation | Layer |
|-------|------------------|-------|
| `ExpenseTrackerApplication` | `@SpringBootApplication` | Entry Point |
| `Expense` | `@Entity` | Model / Domain |
| `ExpenseRepository` | `@Repository` | Data Access (DAO) |
| `ExpenseService` | `@Service` | Business Logic |
| `ExpenseController` | `@Controller` | Presentation / Web |
