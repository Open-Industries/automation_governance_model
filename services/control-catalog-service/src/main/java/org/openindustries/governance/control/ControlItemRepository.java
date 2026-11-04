package org.openindustries.governance.control;

import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/** Outbound port; default in-memory adapter, production adapter is JPA on this service's own database. */
@ApplicationScoped
public class ControlItemRepository {
    private final ConcurrentHashMap<UUID, ControlItem> store = new ConcurrentHashMap<>();

    public ControlItem save(ControlItem entity) {
        store.put(entity.id, entity);
        return entity;
    }

    public Optional<ControlItem> findById(UUID id) {
        return Optional.ofNullable(store.get(id));
    }

    public List<ControlItem> findAll() {
        return List.copyOf(store.values());
    }
}
