package org.openindustries.governance.control;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class ControlItemService {

    @Inject
    ControlItemRepository repository;

    public ControlItem create(CreateControlItemRequest request) {
        return repository.save(ControlItem.create(request.name(), request.controlType(), request.reviewCycleDays()));
    }

    public ControlItem get(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new DomainException(404, "ControlItem not found: " + id));
    }

    public List<ControlItem> list() {
        return repository.findAll();
    }

    public ControlItem suspend(UUID id) {
        ControlItem entity = get(id);
        if (!"ACTIVE".equals(entity.status)) {
            throw new DomainException(409, "Only a ACTIVE controlitem can transition via suspend");
        }
        entity.status = "SUSPENDED";
        return repository.save(entity);
    }
}
