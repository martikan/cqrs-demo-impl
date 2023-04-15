package com.demobank.account.query.api.controller;

import com.demobank.account.common.dto.BaseResponse;
import com.demobank.account.query.api.dto.AccountLookupResponse;
import com.demobank.account.query.api.dto.EqualityType;
import com.demobank.account.query.api.query.FindAccountByBalanceQuery;
import com.demobank.account.query.api.query.FindAccountByHolderQuery;
import com.demobank.account.query.api.query.FindAccountByIdQuery;
import com.demobank.account.query.api.query.FindAllAccountsQuery;
import com.demobank.account.query.domain.BankAccount;
import com.demobank.cqrs.core.exception.AggregateNotFoundException;
import com.demobank.cqrs.core.infra.QueryDispatcher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.MessageFormat;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/bankAccountLookup")
public class AccountLookupController {

    private final QueryDispatcher queryDispatcher;

    @GetMapping
    public ResponseEntity<AccountLookupResponse> getAllAccounts() {
        try {
            final List<BankAccount> accounts = queryDispatcher.send(new FindAllAccountsQuery());

            final var res = AccountLookupResponse.builder()
                    .accounts(accounts)
                    .message(MessageFormat.format("Successfully returned {0} bank account(s)!", accounts.size()))
                    .build();

            return ResponseEntity.ok(res);
        } catch (Exception e) {
            final var safeErrMsg = "failed to complete get all accounts request";

            log.error(safeErrMsg, e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new AccountLookupResponse(HttpStatus.INTERNAL_SERVER_ERROR.toString(), safeErrMsg));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountLookupResponse> getAccountById(@PathVariable(value = "id") String id) {
        try {
            final List<BankAccount> accounts = queryDispatcher.send(new FindAccountByIdQuery(id));
            if (accounts == null || accounts.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            final var res = AccountLookupResponse.builder()
                    .accounts(accounts)
                    .message("Successfully returned bank account!")
                    .build();

            return ResponseEntity.ok(res);
        } catch (Exception e) {
            final var safeErrMsg = "failed to complete get account by id request";

            log.error(safeErrMsg, e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new AccountLookupResponse(HttpStatus.INTERNAL_SERVER_ERROR.toString(), safeErrMsg));
        }
    }

    @GetMapping("/holder/{accountHolder}")
    public ResponseEntity<AccountLookupResponse> getAccountByHolder(@PathVariable(value = "accountHolder") String accountHolder) {
        try {
            final List<BankAccount> accounts = queryDispatcher.send(new FindAccountByHolderQuery(accountHolder));
            if (accounts == null || accounts.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            final var res = AccountLookupResponse.builder()
                    .accounts(accounts)
                    .message("Successfully returned bank account!")
                    .build();

            return ResponseEntity.ok(res);
        } catch (Exception e) {
            final var safeErrMsg = "failed to complete get account by holder request";

            log.error(safeErrMsg, e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new AccountLookupResponse(HttpStatus.INTERNAL_SERVER_ERROR.toString(), safeErrMsg));
        }
    }

    @GetMapping("/withBalance/{equalityType}/{balance}")
    public ResponseEntity<AccountLookupResponse> getAccountByBalance(@PathVariable(value = "equalityType") EqualityType equalityType,
                                                                    @PathVariable(value = "balance") double balance) {
        try {
            final List<BankAccount> accounts = queryDispatcher.send(new FindAccountByBalanceQuery(equalityType, balance));
            if (accounts == null || accounts.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            final var res = AccountLookupResponse.builder()
                    .accounts(accounts)
                    .message(MessageFormat.format("Successfully returned {0} bank account(s)!", accounts.size()))
                    .build();

            return ResponseEntity.ok(res);
        } catch (Exception e) {
            final var safeErrMsg = "failed to complete get account by balance request";

            log.error(safeErrMsg, e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new AccountLookupResponse(HttpStatus.INTERNAL_SERVER_ERROR.toString(), safeErrMsg));
        }
    }

}
