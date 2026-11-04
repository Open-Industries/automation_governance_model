# Execution Budget Service

Bounded context: **Execution Budgets** — Guarded monthly execution budgets per automation and the run charges against them.

Alignment: Governance monitoring: no unbudgeted execution. Core invariant, enforced in code and tests: **the guarded
balance/capacity never goes negative** — over-drawing operations are rejected with `422`
and nothing is recorded. All data is synthetic.

| Item | Value |
|---|---|
| Port | `8082` (`HTTP_PORT`) |
| Base path | `/api/v1/execution-budgets` |
| Owns data | ExecutionBudget accounts, ExecutionCharge records |
| Publishes | `budget.consumed`, `budget.insufficient` |

Run locally: `mvn -pl services/execution-budget-service quarkus:dev`.
