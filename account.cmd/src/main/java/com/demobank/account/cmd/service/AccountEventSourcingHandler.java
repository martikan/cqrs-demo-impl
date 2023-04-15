package com.demobank.account.cmd.service;

import com.demobank.account.cmd.domain.AccountAggregate;
import com.demobank.cqrs.core.domain.AggregateRoot;
import com.demobank.cqrs.core.event.BaseEvent;
import com.demobank.cqrs.core.handler.EventSourcingHandler;
import com.demobank.cqrs.core.infra.EventStore;
import com.demobank.cqrs.core.producer.EventProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;

@RequiredArgsConstructor
@Service
public class AccountEventSourcingHandler implements EventSourcingHandler<AccountAggregate> {

    private final EventStore eventStore;

    private final EventProducer eventProducer;

    @Override
    public void save(AggregateRoot aggregate) {
        eventStore.saveEvents(aggregate.getId(), aggregate.getUncommittedChanges(), aggregate.getVersion());
        aggregate.markChangesAsCommitted();
    }

    @Override
    public AccountAggregate getById(String id) {
        final var aggregate = new AccountAggregate();

        final var events = eventStore.getEvents(id);
        if (events != null && !events.isEmpty()) {
            final var latestVersion = events.stream()
                    .map(this::mapVersion)
                    .max(Comparator.naturalOrder());

            aggregate.replayEvents(events);
            aggregate.setVersion(latestVersion.get());
        }

        return aggregate;
    }

    @Override
    public void republishEvents() {
        final var aggregateIds = eventStore.getAggregateIds();

        for (final var id: aggregateIds) {
            final var aggregate = getById(id);
            if (aggregate == null || !aggregate.isActive()) continue;

            eventStore.getEvents(id).forEach(e -> {
                eventProducer.produce(e.getClass().getSimpleName(), e);
            });
        }

    }

    private int mapVersion(BaseEvent event) {
        return event.getVersion();
    }
}
