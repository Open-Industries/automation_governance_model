# Control Catalog Service

Bounded context: **Controls** — Catalogue of governance controls and their review cycles.

Alignment: COBIT 2019 objectives posture. Lifecycle transitions take no body. All data is synthetic.

| Item | Value |
|---|---|
| Port | `8084` (`HTTP_PORT`) |
| Base path | `/api/v1/controls` |
| Owns data | ControlItem records |

Run locally: `mvn -pl services/control-catalog-service quarkus:dev`.
