package com.demobank.account.cmd.api.command;

import com.demobank.account.common.dto.AccountType;
import com.demobank.cqrs.core.command.BaseCommand;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class OpenAccountCommand extends BaseCommand {
    private String accountHolder;

    private AccountType accountType;

    private double openingBalance;

}
