package com.demobank.account.query.service.handler;

import com.demobank.account.common.event.AccountClosedEvent;
import com.demobank.account.common.event.AccountOpenedEvent;
import com.demobank.account.common.event.FundsDepositedEvent;
import com.demobank.account.common.event.FundsWithdrawEvent;

public interface EventHandler {
    void on(AccountOpenedEvent event);

    void on(AccountClosedEvent event);

    void on(FundsDepositedEvent event);

    void on(FundsWithdrawEvent event);
}
