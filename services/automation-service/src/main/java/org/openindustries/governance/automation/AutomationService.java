package org.openindustries.governance.automation;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class AutomationService {

    @Inject
    AutomationRepository repository;

    public Automation create(CreateAutomationRequest request) {
        return repository.save(Automation.create(request.name(), request.automationType(), request.riskTier()));
    }

    public Automation get(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new DomainException(404, "Automation not found: " + id));
    }

    public List<Automation> list() {
        return repository.findAll();
    }

    public Automation approve(UUID id) {
        Automation entity = get(id);
        if ("RETIRED".equals(entity.status)) {
            throw new DomainException(409, "A retired automation is terminal");
        }
        if ("APPROVED".equals(entity.status)) {
            throw new DomainException(409, "Automation already APPROVED");
        }
        entity.status = "APPROVED";
        return repository.save(entity);
    }

    public Automation retire(UUID id) {
        Automation entity = get(id);
        if ("RETIRED".equals(entity.status)) {
            throw new DomainException(409, "A retired automation is terminal");
        }
        if ("RETIRED".equals(entity.status)) {
            throw new DomainException(409, "Automation already RETIRED");
        }
        entity.status = "RETIRED";
        return repository.save(entity);
    }
}
