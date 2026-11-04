package org.openindustries.governance.governance;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class GovernanceRuleService {

    @Inject
    GovernanceRuleRepository repository;

    public GovernanceRule create(CreateGovernanceRuleRequest request) {
        return repository.save(GovernanceRule.create(request.name(), request.riskTier(),
                request.maxRunsPerMonth(), request.maxFailurePct(), request.priority()));
    }

    public GovernanceRule get(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new DomainException(404, "GovernanceRule not found: " + id));
    }

    public List<GovernanceRule> list() {
        return repository.findAll();
    }

    public GovernanceRule activate(UUID id) {
        GovernanceRule rule = get(id);
        rule.status = "ACTIVE";
        return repository.save(rule);
    }

    public Evaluation evaluate(EvaluateRequest request) {
        var candidates = repository.activeFor(request.riskTier());
        if (candidates.isEmpty()) {
            return new Evaluation("DENIED", null,
                    "No active rule for " + request.riskTier());
        }
        for (GovernanceRule rule : candidates) {
            if (rule.maxFailurePct <= request.requiredMaxFailurePct()
                    && rule.maxRunsPerMonth >= request.requiredMaxRunsPerMonth()) {
                return new Evaluation("ALLOWED", rule.id,
                        "Rule '" + rule.name + "' covers the request");
            }
        }
        return new Evaluation("DEGRADED", candidates.get(0).id,
                "Active rules exist for " + request.riskTier() + " but none meets the requirement");
    }
}
