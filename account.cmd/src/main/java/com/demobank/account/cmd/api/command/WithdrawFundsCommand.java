package com.demobank.account.cmd.api.command;

import com.demobank.cqrs.core.command.BaseCommand;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class WithdrawFundsCommand extends BaseCommand {
    private double amount;
}
