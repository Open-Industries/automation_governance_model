# Domain Standards — Automation Governance

Alignment mapping for this core. *Aligned to* throughout: governance accountability
stays with the organization's governing body (ISO/IEC 38500); this platform is its
technical evidence and enforcement surface.

## Concept mapping

| Platform concept | Domain standard / framework | Mapping |
|---|---|---|
| Governance cycle | ISO/IEC 38500 (Evaluate–Direct–Monitor) | Registry + rules (direct), budgets (monitor), verdicts (evaluate) |
| Control catalogue | COBIT 2019 governance/management objectives | Control items typed by governance function |
| Security controls | ISO/IEC 27001/27002 | Interface mapped in `docs/security/` |
| Policy decisions | Policy-as-code practice | Rules context as the single decision point with reasoned outcomes |

## Scope boundaries

This core governs automations; it does not execute them. Execution platforms (CI,
schedulers, RPA) integrate at the boundary and report usage for budget charging.
