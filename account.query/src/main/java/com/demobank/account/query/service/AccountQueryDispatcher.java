package com.demobank.account.query.service;

import com.demobank.cqrs.core.domain.BaseEntity;
import com.demobank.cqrs.core.infra.QueryDispatcher;
import com.demobank.cqrs.core.query.BaseQuery;
import com.demobank.cqrs.core.query.QueryHandlerMethod;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@ Service
public class AccountQueryDispatcher implements QueryDispatcher {

    private final Map<Class<? extends BaseQuery>, List<QueryHandlerMethod>> routes = new HashMap<>();

    @Override
    public <T extends BaseQuery> void registerHandler(Class<T> type, QueryHandlerMethod<T> handler) {
        final var handlers = routes.computeIfAbsent(type, c -> new LinkedList<>());
        handlers.add(handler);
    }

    @Override
    public <U extends BaseEntity> List<U> send(BaseQuery query) {
        final var handlers = routes.get(query.getClass());
        if (handlers == null || handlers.size() == 0) {
            throw new RuntimeException("no query handler was registered!");
        }
        if (handlers.size() > 1) {
            throw new RuntimeException("cannot send query to more than one handler");
        }

        return handlers.get(0).handle(query);
    }
}
