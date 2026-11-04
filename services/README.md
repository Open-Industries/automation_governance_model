# Services

One independently deployable Quarkus microservice per bounded context. Every service
follows the same hexagonal layout, exposes `/openapi`, `/q/health` and `/q/metrics`,
returns RFC 9457 problem details, and is configured only through environment variables
(Twelve-Factor). Lifecycle transitions take no body; `@Consumes` appears only on
endpoints that read one.

**Implementation order** (see `../docs/implementation-roadmap.md`):
Automations → Execution Budgets → Governance Rules → Open APIs.

| Service | Port | Alignment |
|---|---|---|
| `automation-service` | 8081 | ISO/IEC 38500 direct/monitor posture |
| `execution-budget-service` | 8082 | Governance monitoring: no unbudgeted execution |
| `governance-rules-service` | 8083 | Policy-as-code decision point; COBIT APO/BAI posture |
| `control-catalog-service` | 8084 | COBIT 2019 objectives posture |
| `notification-service` | 8085 | Platform support (shared pattern) |

Run the pilot service:

```bash
mvn -pl services/automation-service quarkus:dev
# OpenAPI UI: http://localhost:8081/docs — health: http://localhost:8081/q/health
```
