package org.openindustries.governance.budget;

import java.util.UUID;

/**
 * ExecutionBudget aggregate — the guarded resource account of this core: a balance/capacity that
 * consumption operations draw from. Core invariant (ADR-0004): it NEVER goes negative;
 * an operation that would overdraw it is rejected and records nothing.
 */
public class ExecutionBudget {
    public UUID id;
        public java.util.UUID automationId;
    public long monthlyRuns;
    public String status;

    public static ExecutionBudget open(java.util.UUID automationId, long monthlyRuns) {
        ExecutionBudget a = new ExecutionBudget();
        a.id = UUID.randomUUID();
        a.automationId = automationId;
        a.monthlyRuns = monthlyRuns;
        a.status = "ACTIVE";
        return a;
    }
}
