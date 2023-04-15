package com.demobank.account.query.service.handler;

import com.demobank.account.common.event.AccountClosedEvent;
import com.demobank.account.common.event.AccountOpenedEvent;
import com.demobank.account.common.event.FundsDepositedEvent;
import com.demobank.account.common.event.FundsWithdrawEvent;
import com.demobank.account.query.domain.BankAccount;
import com.demobank.account.query.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

@RequiredArgsConstructor
@Service
public class AccountEventHandler implements EventHandler {

    private final AccountRepository accountRepository;

    @Override
    public void on(AccountOpenedEvent event) {
        final var account = BankAccount.builder()
                .id(event.getId())
                .accountHolder(event.getAccountHolder())
                .accountType(event.getAccountType())
                .balance(event.getOpeningBalance())
                .createdAt(event.getCreatedDate().toInstant().atZone(ZoneOffset.UTC))
                .build();

        accountRepository.save(account);
    }

    @Override
    public void on(AccountClosedEvent event) {
        accountRepository.deleteById(event.getId());
    }

    @Override
    public void on(FundsDepositedEvent event) {
        final var account = accountRepository.findById(event.getId());
        if (account.isEmpty()) return;

        final var currentBalance = account.get().getBalance();

        final var latestBalance = currentBalance + event.getAmount();

        account.get().setBalance(latestBalance);

        accountRepository.save(account.get());
    }

    @Override
    public void on(FundsWithdrawEvent event) {
        final var account = accountRepository.findById(event.getId());
        if (account.isEmpty()) return;

        final var currentBalance = account.get().getBalance();

        final var latestBalance = currentBalance - event.getAmount();

        account.get().setBalance(latestBalance);

        accountRepository.save(account.get());
    }
}
