package com.demobank.account.query.api.query;

import com.demobank.account.query.api.dto.EqualityType;
import com.demobank.account.query.domain.BankAccount;
import com.demobank.account.query.repository.AccountRepository;
import com.demobank.cqrs.core.domain.BaseEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Service
public class AccountQueryHandler implements QueryHandler {

    private final AccountRepository accountRepository;

    @Override
    public List<BaseEntity> handle(FindAllAccountsQuery query) {
        return accountRepository.findAll().stream()
                .map(this::mapAccount)
                .toList();
    }

    @Override
    public List<BaseEntity> handle(FindAccountByIdQuery query) {
        return Stream.of(findById(query))
                .toList();
    }

    @Override
    public List<BaseEntity> handle(FindAccountByHolderQuery query) {
        return Stream.of(findByHolder(query))
                .toList();
    }

    @Override
    public List<BaseEntity> handle(FindAccountByBalanceQuery query) {
        return query.getEqualityType() == EqualityType.GREATER_THAN
                ? accountRepository.findByBalanceGreaterThan(query.getBalance())
                : accountRepository.findByBalanceLessThan(query.getBalance());
    }

    private BaseEntity findById(FindAccountByIdQuery query) {
        return accountRepository.findById(query.getId())
                .orElse(null);
    }

    private BaseEntity findByHolder(FindAccountByHolderQuery query) {
        return accountRepository.findFirstByAccountHolder(query.getAccountHolder())
                .orElse(null);
    }

    private BaseEntity mapAccount(final BankAccount account) {
        return account;
    }

}
