package org.openindustries.governance.budget;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class BudgetService {

    @Inject
    BudgetRepository repository;

    public ExecutionBudget open(OpenExecutionBudgetRequest request) {
        return repository.saveAccount(ExecutionBudget.open(request.automationId(), request.monthlyRuns()));
    }

    public ExecutionBudget get(UUID id) {
        return repository.findAccount(id)
                .orElseThrow(() -> new DomainException(404, "ExecutionBudget not found: " + id));
    }

    /**
     * Draws from the account. Invariant: the balance/capacity never goes negative —
     * an over-drawing operation is rejected (422) and nothing is recorded.
     */
    public ExecutionCharge consume(UUID accountId, CreateExecutionChargeRequest request) {
        ExecutionBudget account = get(accountId);
        if (!"ACTIVE".equals(account.status)) {
            throw new DomainException(409, "ExecutionBudget is not ACTIVE");
        }
        if (request.runs() > account.monthlyRuns) {
            throw new DomainException(422, "Insufficient capacity: requested " + request.runs()
                    + " exceeds available " + account.monthlyRuns);
        }
        account.monthlyRuns -= request.runs();
        repository.saveAccount(account);
        return repository.saveRecord(ExecutionCharge.of(accountId, request.runType(), request.runs(), account.monthlyRuns));
    }

    public List<ExecutionCharge> recordsOf(UUID accountId) {
        get(accountId);
        return repository.recordsOf(accountId);
    }
}
