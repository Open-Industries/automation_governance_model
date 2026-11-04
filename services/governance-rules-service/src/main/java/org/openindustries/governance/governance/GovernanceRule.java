package org.openindustries.governance.governance;

import java.util.UUID;

/**
 * GovernanceRule aggregate — a decision rule of this core: for one riskTier, it guarantees a
 * capacity (maxRunsPerMonth) and a threshold (maxFailurePct), with a precedence priority.
 * Only ACTIVE rules decide; every decision names its rule and its reason (audit).
 */
public class GovernanceRule {
    public UUID id;
    public String name;
    public String riskTier;
    public long maxRunsPerMonth;
    public int maxFailurePct;
    public int priority;
    public String status;

    public static GovernanceRule create(String name, String riskTier, long maxRunsPerMonth, int maxFailurePct, int priority) {
        GovernanceRule r = new GovernanceRule();
        r.id = UUID.randomUUID();
        r.name = name;
        r.riskTier = riskTier;
        r.maxRunsPerMonth = maxRunsPerMonth;
        r.maxFailurePct = maxFailurePct;
        r.priority = priority;
        r.status = "DRAFT";
        return r;
    }
}
