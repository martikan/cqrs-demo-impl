package com.demobank.account.cmd;

import com.demobank.account.cmd.api.command.CloseAccountCommand;
import com.demobank.account.cmd.api.command.CommandHandler;
import com.demobank.account.cmd.api.command.DepositFundsCommand;
import com.demobank.account.cmd.api.command.OpenAccountCommand;
import com.demobank.account.cmd.api.command.RestoreReadDBCommand;
import com.demobank.account.cmd.api.command.WithdrawFundsCommand;
import com.demobank.cqrs.core.infra.CommandDispatcher;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableMongoRepositories
@RequiredArgsConstructor
@SpringBootApplication
public class CommandApplication {

	private final CommandDispatcher commandDispatcher;

	private final CommandHandler commandHandler;

	public static void main(String[] args) {
		SpringApplication.run(CommandApplication.class, args);
	}

	@PostConstruct
	public void registerHandlers() {
		commandDispatcher.registerHandler(OpenAccountCommand.class, commandHandler::handle);
		commandDispatcher.registerHandler(CloseAccountCommand.class, commandHandler::handle);
		commandDispatcher.registerHandler(DepositFundsCommand.class, commandHandler::handle);
		commandDispatcher.registerHandler(WithdrawFundsCommand.class, commandHandler::handle);
		commandDispatcher.registerHandler(RestoreReadDBCommand.class, commandHandler::handle);
	}

}
