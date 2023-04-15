package com.demobank.account.cmd.api.command;

public interface CommandHandler {
    void handle(OpenAccountCommand command);

    void handle(CloseAccountCommand command);

    void handle(DepositFundsCommand command);

    void handle(WithdrawFundsCommand command);

    void handle(RestoreReadDBCommand command);
}
