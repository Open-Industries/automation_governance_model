# Governance Rules Service

Bounded context: **Governance Rules** — Decision rules binding risk tiers to run-volume and failure envelopes.

Alignment: Policy-as-code decision point; COBIT APO/BAI posture. Decisions are `ALLOWED` / `DEGRADED` / `DENIED`, always
with the matched rule and a human-readable reason for audit. All data is synthetic.

| Item | Value |
|---|---|
| Port | `8083` (`HTTP_PORT`) |
| Base path | `/api/v1/governance-rules` |
| Owns data | GovernanceRule rules |
| Publishes | `governance.activated` |

Run locally: `mvn -pl services/governance-rules-service quarkus:dev`.
