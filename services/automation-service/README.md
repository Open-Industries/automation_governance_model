# Automation Service

Bounded context: **Automations** — Registry of governed automations with risk tiers and approval lifecycle.

Alignment: ISO/IEC 38500 direct/monitor posture. Lifecycle transitions take no body; secret identifiers,
where the domain has them, are masked at creation and never stored or returned in clear.
All data is synthetic.

| Item | Value |
|---|---|
| Port | `8081` (`HTTP_PORT`) |
| Base path | `/api/v1/automations` |
| Owns data | Automation records |
| Publishes | `automation.created`, lifecycle events (see `contracts/asyncapi.yaml`) |

Run locally: `mvn -pl services/automation-service quarkus:dev`.
