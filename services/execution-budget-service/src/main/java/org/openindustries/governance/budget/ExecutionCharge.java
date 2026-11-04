package org.openindustries.governance.budget;

import java.time.Instant;
import java.util.UUID;

/** One guarded consumption record — the auditable evidence of a draw on a ExecutionBudget. */
public class ExecutionCharge {
    public UUID id;
    public UUID accountId;
    public String runType;
    public long runs;
    public long remainingAfter;
    public Instant recordedAt;

    public static ExecutionCharge of(UUID accountId, String runType, long runs, long remainingAfter) {
        ExecutionCharge r = new ExecutionCharge();
        r.id = UUID.randomUUID();
        r.accountId = accountId;
        r.runType = runType;
        r.runs = runs;
        r.remainingAfter = remainingAfter;
        r.recordedAt = Instant.now();
        return r;
    }
}
