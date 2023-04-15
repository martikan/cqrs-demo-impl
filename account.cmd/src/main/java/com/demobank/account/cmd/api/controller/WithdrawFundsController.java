package com.demobank.account.cmd.api.controller;

import com.demobank.account.cmd.api.command.WithdrawFundsCommand;
import com.demobank.account.cmd.api.dto.OpenAccountResponse;
import com.demobank.account.common.dto.BaseResponse;
import com.demobank.cqrs.core.exception.AggregateNotFoundException;
import com.demobank.cqrs.core.infra.CommandDispatcher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.MessageFormat;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/withdrawFunds")
public class WithdrawFundsController {

    private final CommandDispatcher commandDispatcher;

    @PutMapping("{id}")
    public ResponseEntity<BaseResponse> withdrawFunds(@PathVariable(value = "id") String id,
                                                     @RequestBody WithdrawFundsCommand command) {
        try {
            command.setId(id);

            commandDispatcher.send(command);

            return ResponseEntity.ok(new BaseResponse(HttpStatus.OK.toString(), "bank account withdraw funds request completed successfully!"));
        } catch (IllegalStateException | AggregateNotFoundException e) {
            log.warn(MessageFormat.format("client made a bad request - {0}.", e));

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new BaseResponse(HttpStatus.BAD_REQUEST.toString(), e.getMessage()));
        } catch (Exception e) {
            final var safeErrMsg = MessageFormat.format("error while processing request to withdraw funds to bank account for id - {0}.", id);

            log.error(safeErrMsg, e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new OpenAccountResponse(HttpStatus.INTERNAL_SERVER_ERROR.toString(), safeErrMsg, id));
        }
    }

}
