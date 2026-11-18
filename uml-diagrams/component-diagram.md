# Component Diagram – Expense Tracker

> Shows major architectural components, their interfaces, and runtime dependencies.

```mermaid
flowchart TD
    subgraph Client["🌐 Client Browser"]
        B["Browser\n(HTML / HTTP)"]
    end

    subgraph SpringBoot["☕ Spring Boot Application"]
        direction TB

        subgraph WebLayer["Web Layer"]
            EC["ExpenseController\n@Controller"]
        end

        subgraph ServiceLayer["Service Layer"]
            ES["ExpenseService\n@Service"]
        end

        subgraph DataLayer["Data Access Layer"]
            ER["ExpenseRepository\n@Repository\nextends JpaRepository"]
        end

        subgraph DomainLayer["Domain / Model"]
            EX["Expense\n@Entity"]
        end

        subgraph SpringFramework["Spring Framework Infrastructure"]
            DI["Spring IoC Container\nDependency Injection"]
            MVC["Spring MVC\nDispatcherServlet"]
            JPA["Spring Data JPA\nHibernate ORM"]
            AC["Spring Boot AutoConfig"]
        end

        subgraph Templates["View Templates"]
            TH["Thymeleaf Templates\nindex.html\nadd-expense.html\nupdate-expense.html"]
        end
    end

    subgraph DB["💾 Database"]
        H2["H2 In-Memory DB\njdbc:h2:mem:testdb\nExpense TABLE"]
    end

    %% External flow
    B -- "HTTP GET / POST" --> MVC
    MVC -- "routes request" --> EC
    EC -- "calls" --> ES
    ES -- "calls" --> ER
    ER -- "SQL via Hibernate" --> H2
    H2 -- "ResultSet" --> ER
    ER -- "Expense entities" --> ES
    ES -- "Expense list / entity" --> EC
    EC -- "Model + view name" --> TH
    TH -- "HTML Response" --> B

    %% Spring wiring
    DI -. "@Autowired injection" .-> EC
    DI -. "@Autowired injection" .-> ES
    DI -. "@Autowired injection" .-> ER
    AC -. "auto-configures" .-> JPA
    AC -. "auto-configures" .-> MVC
    JPA -. "manages entity" .-> EX
    JPA -. "implements" .-> ER
```

## Component Responsibilities

| Component | Technology | Responsibility |
|-----------|-----------|----------------|
| **ExpenseController** | Spring MVC `@Controller` | Handles HTTP requests, maps routes, calls service |
| **ExpenseService** | Spring `@Service` | Business logic, orchestrates repository calls |
| **ExpenseRepository** | Spring Data JPA `@Repository` | CRUD operations on `Expense` table |
| **Expense** | JPA `@Entity` | Domain model mapped to DB table |
| **Thymeleaf Templates** | Thymeleaf | Server-side HTML rendering |
| **H2 Database** | H2 In-Memory | Stores expense records at runtime |
| **Spring IoC Container** | Spring Core | Manages beans, injects dependencies |
| **Spring MVC** | Spring Web | DispatcherServlet routes HTTP requests |
| **Spring Data JPA** | Hibernate | ORM, generates SQL from entity model |
