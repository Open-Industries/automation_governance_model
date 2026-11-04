package org.openindustries.governance.governance;

import jakarta.enterprise.context.ApplicationScoped;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/** Outbound port; default in-memory adapter, production adapter is JPA on this service's own database. */
@ApplicationScoped
public class GovernanceRuleRepository {
    private final ConcurrentHashMap<UUID, GovernanceRule> store = new ConcurrentHashMap<>();

    public GovernanceRule save(GovernanceRule rule) {
        store.put(rule.id, rule);
        return rule;
    }

    public Optional<GovernanceRule> findById(UUID id) {
        return Optional.ofNullable(store.get(id));
    }

    public List<GovernanceRule> findAll() {
        return List.copyOf(store.values());
    }

    /** Active rules for a scope, highest priority (lowest number) first. */
    public List<GovernanceRule> activeFor(String riskTier) {
        return store.values().stream()
                .filter(r -> r.riskTier.equals(riskTier) && "ACTIVE".equals(r.status))
                .sorted(Comparator.comparingInt(r -> r.priority))
                .toList();
    }
}
