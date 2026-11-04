# Open Automation Governance — Reference Governance Core Platform

A **standard, generic automation governance core platform** that any organization in this vertical
can deploy and adapt: built as **microservices and serverless functions** on
**Java 21 + Quarkus**, aligned with **The Twelve-Factor App**, documented under the ISO
architecture and quality standards, and aligned with the domain standards of the
vertical (see the traceability matrix). Everything is *aligned to* the cited standards —
**no certification, conformance or regulatory-compliance claim is made**. All data is
**synthetic**.

> Reference implementation: small, complete and runnable. The rules that matter in this
> domain are enforced in code and covered by tests — start with the invariant:
> executions charged against an automation's approved monthly budget never exceed that budget — a charge that would overrun it is rejected (422) and records nothing; no automation runs unbudgeted or uncounted.

## Standards alignment

| Standard / framework | What it governs here | Where |
|---|---|---|
| ISO/IEC/IEEE **42010** | Architecture description | `docs/architecture/architecture-description.md` |
| ISO/IEC **25010** (SQuaRE) | Product quality model and scenarios | `docs/architecture/quality-attributes.md` |
| ISO/IEC/IEEE **15288 & 12207** | Life-cycle processes | `docs/lifecycle/software-lifecycle.md` |
| ISO/IEC **27001 & 27002** | Information security | `docs/security/` |
| **The Twelve-Factor App** | Cloud-native methodology | `docs/architecture/twelve-factor.md` |
| ISO/IEC 38500 | Governance of IT for the organization | `docs/domain/domain-standards.md` mapping (EDM cycle) |
| COBIT 2019 | Governance & management objectives | Control catalogue posture (EDM/APO/BAI/DSS/MEA alignment) |
| ISO/IEC 27001/27002 | Information security controls | Control interface in `docs/security/` |
| Policy-as-code practice | Machine-checkable governance | Rules context as the policy decision point |

Full traceability: `docs/compliance/standards-traceability.md`.

## The platform at a glance

### Microservices (Quarkus, `services/`)

| Service | Port | Alignment | Responsibility |
|---|---|---|---|
| `automation-service` | 8081 | ISO/IEC 38500 direct/monitor posture | Registry of governed automations with risk tiers and approval lifecycle. |
| `execution-budget-service` | 8082 | Governance monitoring: no unbudgeted execution | Guarded monthly execution budgets per automation and the run charges against them. |
| `governance-rules-service` | 8083 | Policy-as-code decision point; COBIT APO/BAI posture | Decision rules binding risk tiers to run-volume and failure envelopes. |
| `control-catalog-service` | 8084 | COBIT 2019 objectives posture | Catalogue of governance controls and their review cycles. |
| `notification-service` | 8085 | Platform support (shared pattern) | Channel-agnostic dispatch of domain notifications from events. |

### Serverless functions (Quarkus Funqy + Knative, `functions/`)

| Function | Trigger | Responsibility |
|---|---|---|
| `automation-risk-scorer` | Event / HTTP | Scores automation risk signals (failures, privilege breadth, change rate) with an auditable rationale. |
| `governance-compliance-evaluator` | Event / HTTP | Compares overdue reviews and evidence coverage against automation targets; one finding per breach. |

## Design principles

1. **One bounded context per service**, identical hexagonal layout everywhere.
2. **The domain invariant is code, not prose** — executions charged against an automation's approved monthly budget never exceed that budget — a charge that would overrun it is rejected (422) and records nothing; no automation runs unbudgeted or uncounted.
3. **Privacy by design** — Governance data is organizational, not personal: owners are teams, not individuals, in every contract. Credentials of governed automations never enter this platform; all data is synthetic.
4. **Database per service, API-first (OpenAPI), events as CloudEvents.**
5. **Adapt by configuration** — rules, thresholds, tariffs/rates and providers are data
   and configuration, not code forks (see *Adaptation guide*).

## Quick start

Prerequisites: Java 21, Maven 3.9+, optionally Docker/Podman and Knative/OpenShift.

```bash
mvn verify                                    # build and test everything
mvn -pl services/automation-service quarkus:dev
docker compose up -d                          # local PostgreSQL, Kafka, Keycloak
```

## Deployment

- **Kubernetes / OpenShift**: `kubectl apply -k deploy/kubernetes/`.
- **Serverless**: Knative Services in `deploy/knative/` (scale to zero).
- **Local**: `docker-compose.yml` for dependencies; services run with `quarkus:dev`.

## Adaptation guide

| You want to… | Change only… |
|---|---|
| Adjust domain rules and thresholds | Rule data in the `governance-rules-service` context and function configuration |
| Change the guarded capacity semantics | The `execution-budget-service` context (invariant stays: never negative) |
| Plug your IdP / providers | OIDC at the gateway; provider adapters at the boundary |
| Replace in-memory persistence | The repository port's JPA adapter on the service's own database (`DB_KIND`, `DB_URL`) |

## Documentation map

- Architecture (42010): `docs/architecture/` · Decisions: `docs/adr/`
- Domain standards: `docs/domain/domain-standards.md`
- Quality (25010), Security (27001/27002), Life cycle (15288/12207), API, Data, Operations: under `docs/`

## License

Apache-2.0 — see `LICENSE`.
