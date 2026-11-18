# Object Diagram – Expense Tracker

> Shows concrete runtime instances and their linked values during a typical "view home page" request.

```mermaid
classDiagram
    direction LR

    class `app : ExpenseTrackerApplication` {
        <<instance>>
    }

    class `controller : ExpenseController` {
        <<instance>>
        expenseService = serviceInstance
    }

    class `service : ExpenseService` {
        <<instance>>
        expenseRepository = repoInstance
    }

    class `repository : ExpenseRepository` {
        <<instance>>
    }

    class `expense1 : Expense` {
        <<instance>>
        id = 1
        description = "Groceries"
        amount = 450.00
    }

    class `expense2 : Expense` {
        <<instance>>
        id = 2
        description = "Electricity Bill"
        amount = 1200.00
    }

    class `expense3 : Expense` {
        <<instance>>
        id = 3
        description = "Internet Subscription"
        amount = 599.00
    }

    class `model : Model` {
        <<instance>>
        expenses = [expense1, expense2, expense3]
        totalAmount = 2249.00
    }

    `app : ExpenseTrackerApplication` --> `controller : ExpenseController` : creates/manages
    `controller : ExpenseController` --> `service : ExpenseService` : delegates to
    `service : ExpenseService` --> `repository : ExpenseRepository` : queries
    `repository : ExpenseRepository` --> `expense1 : Expense` : returns
    `repository : ExpenseRepository` --> `expense2 : Expense` : returns
    `repository : ExpenseRepository` --> `expense3 : Expense` : returns
    `controller : ExpenseController` --> `model : Model` : populates
```

## Runtime State Description

| Instance | Type | Key State |
|----------|------|-----------|
| `app` | `ExpenseTrackerApplication` | Spring context bootstrapped |
| `controller` | `ExpenseController` | Holds injected `service` reference |
| `service` | `ExpenseService` | Holds injected `repository` reference |
| `repository` | `ExpenseRepository` | Proxy managed by Spring Data JPA |
| `expense1` | `Expense` | id=1, "Groceries", ₹450.00 |
| `expense2` | `Expense` | id=2, "Electricity Bill", ₹1200.00 |
| `expense3` | `Expense` | id=3, "Internet Subscription", ₹599.00 |
| `model` | `org.springframework.ui.Model` | expenses list + totalAmount=₹2249.00 |
