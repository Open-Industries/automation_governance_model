# Governance Compliance Evaluator (serverless function)

Quarkus Funqy function on Knative (scale to zero). Compares overdue reviews and evidence coverage against automation targets; one finding per breach.

Invoke: `POST /governanceComplianceEvaluator` with the KPI payload.
