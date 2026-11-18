# Use Case Diagram – Expense Tracker

> Shows the actors and all supported system use cases.

```mermaid
flowchart LR
    User(["👤 User\n(Actor)"])
    Spring(["⚙️ Administrator\n(Behind the Scene Actor)"])

    subgraph System["Expense Tracker System"]
        UC1["View All Expenses\nGET /"]
        UC2["Add New Expense\nGET /addExpense\nPOST /saveExpense"]
        UC3["Edit Expense\nGET /editExpense/{id}\nPOST /updateExpense/{id}"]
        UC4["Delete Expense\nGET /deleteExpense/{id}"]
        UC5["View Total Expense Amount\nGET /"]
        UC6["Persist Expense to DB\n(save / update / delete)"]
        UC7["Fetch Expenses from DB\n(findAll / findById)"]
        UC8["Auto-create DB Schema\n(ddl-auto=update)"]
        UC9["Access H2 Console\n/h2-console"]
    end

    %% User interactions
    User --> UC1
    User --> UC2
    User --> UC3
    User --> UC4
    User --> UC5
    User --> UC9

    %% System-internal interactions
    UC1 -->|"includes"| UC7
    UC2 -->|"includes"| UC6
    UC3 -->|"includes"| UC7
    UC3 -->|"includes"| UC6
    UC4 -->|"includes"| UC6
    UC5 -->|"extends"| UC1

    Spring --> UC8

    %% Styling
    style System fill:#f0f8ff,stroke:#4a90d9,stroke-width:2px
    style User fill:#fffde7,stroke:#f9a825,stroke-width:2px
    style Spring fill:#f3e5f5,stroke:#7b1fa2,stroke-width:2px
```

## Use Case Descriptions

| Use Case | Actor | Description | HTTP Route |
|----------|-------|-------------|-----------|
| **View All Expenses** | User | Displays a list of all saved expenses | `GET /` |
| **View Total Amount** | User | Shows the sum of all expense amounts | `GET /` (extends View All) |
| **Add New Expense** | User | Opens form and submits a new expense entry | `GET /addExpense` → `POST /saveExpense` |
| **Edit Expense** | User | Opens pre-filled form and submits updated values | `GET /editExpense/{id}` → `POST /updateExpense/{id}` |
| **Delete Expense** | User | Removes an expense record by its ID | `GET /deleteExpense/{id}` |
| **Persist Expense to DB** | System | Save/update/delete an Expense record via JPA | Internal |
| **Fetch Expenses from DB** | System | Retrieve expense records via JPA | Internal |
| **Auto-create DB Schema** | Spring | Hibernate creates/updates `expense` table on startup | Internal |
| **Access H2 Console** | User | Opens the in-memory H2 database admin console | `GET /h2-console` |
