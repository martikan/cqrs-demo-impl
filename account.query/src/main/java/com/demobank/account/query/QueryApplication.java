package com.demobank.account.query;

import com.demobank.account.query.api.query.FindAccountByBalanceQuery;
import com.demobank.account.query.api.query.FindAccountByHolderQuery;
import com.demobank.account.query.api.query.FindAccountByIdQuery;
import com.demobank.account.query.api.query.FindAllAccountsQuery;
import com.demobank.account.query.api.query.QueryHandler;
import com.demobank.cqrs.core.infra.QueryDispatcher;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@RequiredArgsConstructor
@SpringBootApplication
public class QueryApplication {

	private final QueryDispatcher queryDispatcher;

	private final QueryHandler queryHandler;

	public static void main(String[] args) {
		SpringApplication.run(QueryApplication.class, args);
	}

	@PostConstruct
	public void registerHandlers() {
		queryDispatcher.registerHandler(FindAllAccountsQuery.class, queryHandler::handle);
		queryDispatcher.registerHandler(FindAccountByIdQuery.class, queryHandler::handle);
		queryDispatcher.registerHandler(FindAccountByHolderQuery.class, queryHandler::handle);
		queryDispatcher.registerHandler(FindAccountByBalanceQuery.class, queryHandler::handle);
	}

}
