package com.demobank.account.cmd.service;

import com.demobank.account.cmd.domain.AccountAggregate;
import com.demobank.account.cmd.repository.EventStoreRepository;
import com.demobank.cqrs.core.event.EventModel;
import com.demobank.cqrs.core.exception.AggregateNotFoundException;
import com.demobank.cqrs.core.exception.ConcurrencyException;
import com.demobank.cqrs.core.event.BaseEvent;
import com.demobank.cqrs.core.infra.EventStore;
import com.demobank.cqrs.core.producer.EventProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Service
public class AccountEventStore implements EventStore {

    private final EventStoreRepository eventStoreRepository;

    private final EventProducer eventProducer;

    private final ConversionService conversionService;

    @Override
    public void saveEvents(String aggregateId, Iterable<BaseEvent> events, int expectedVersion) {
        final var eventStream = eventStoreRepository.findByAggregateId(aggregateId);
        if (expectedVersion != -1 && eventStream.get(eventStream.size() -1).getVersion() != expectedVersion) {
            throw new ConcurrencyException();
        }

        var version = expectedVersion;
        for (var e: events) {
            e.setVersion(++version);

            final var event = EventModel.builder()
                    .aggregateId(aggregateId)
                    .aggregateType(AccountAggregate.class.getTypeName())
                    .version(version)
                    .eventType(e.getClass().getTypeName())
                    .payload(e)
                    .createdAt(conversionService.convert(ZonedDateTime.now(), Date.class))
                    .build();

            final var savedEvent = eventStoreRepository.save(event);
            if (!savedEvent.getId().isBlank()) {
                eventProducer.produce(event.getClass().getSimpleName(), e);
            }
        }

    }

    @Override
    public List<BaseEvent> getEvents(String aggregateId) {
        final var eventStream = eventStoreRepository.findByAggregateId(aggregateId);
        if (eventStream == null || eventStream.isEmpty()) {
            throw new AggregateNotFoundException("incorrect account id was provided");
        }

        return eventStream.stream()
                .map(this::mapEvent)
                .toList();
    }

    @Override
    public List<String> getAggregateIds() {
        final var eventStream = eventStoreRepository.findAll();
        if (eventStream.isEmpty()) {
            throw new IllegalStateException("could not retrieve event stream from the event store");
        }

        return eventStream.stream()
                .map(EventModel::getAggregateId)
                .distinct()
                .toList();
    }

    private BaseEvent mapEvent(final EventModel eventModel) {
        return eventModel.getPayload();
    }
}
