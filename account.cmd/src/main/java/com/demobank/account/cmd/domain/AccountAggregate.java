package com.demobank.account.cmd.domain;

import com.demobank.account.cmd.api.command.OpenAccountCommand;
import com.demobank.account.common.event.AccountClosedEvent;
import com.demobank.account.common.event.AccountOpenedEvent;
import com.demobank.account.common.event.FundsDepositedEvent;
import com.demobank.account.common.event.FundsWithdrawEvent;
import com.demobank.cqrs.core.domain.AggregateRoot;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.time.ZonedDateTime;

@Getter
@NoArgsConstructor
public class AccountAggregate extends AggregateRoot {

    private boolean active;

    private double balance;

    public AccountAggregate(OpenAccountCommand command) {
        final var event = AccountOpenedEvent.builder()
                .id(command.getId())
                .accountHolder(command.getAccountHolder())
                .accountType(command.getAccountType())
                .openingBalance(command.getOpeningBalance())
                .createdDate(Date.from(ZonedDateTime.now().toInstant()))
                .build();

        raiseEvent(event);
    }

    public void apply(AccountOpenedEvent event) {
        this.id = event.getId();
        this.active = true;
        this.balance = event.getOpeningBalance();
    }

    public void depositFunds(double amount) {
        if (!this.active) {
            throw new IllegalStateException("funds cannot be deposited into a closed account");
        }

        if (amount <= 0) {
            throw new IllegalStateException("the deposit amount must be greater then 0");
        }

        final var event = FundsDepositedEvent.builder()
                .id(this.id)
                .amount(amount)
                .build();

        raiseEvent(event);
    }

    public void apply(FundsDepositedEvent event) {
        this.id = event.getId();
        this.balance += event.getAmount();
    }

    public void withdrawFunds(double amount) {
        if (!this.active) {
            throw new IllegalStateException("funds cannot be deposited from a closed account");
        }

        final var event = FundsWithdrawEvent.builder()
                .id(this.id)
                .amount(amount)
                .build();

        raiseEvent(event);
    }

    public void apply(FundsWithdrawEvent event) {
        this.id = event.getId();
        this.balance -= event.getAmount();
    }

    public void closeAccount() {
        if (!this.active) {
            throw new IllegalStateException("the account has been already closed");
        }

        final var event = AccountClosedEvent.builder()
                .id(this.id)
                .build();

        raiseEvent(event);
    }

    public void apply(AccountClosedEvent event) {
        this.id = event.getId();
        this.active = false;
    }
}
