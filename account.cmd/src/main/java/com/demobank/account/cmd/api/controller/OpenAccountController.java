package com.demobank.account.cmd.api.controller;

import com.demobank.account.cmd.api.command.OpenAccountCommand;
import com.demobank.account.cmd.api.dto.OpenAccountResponse;
import com.demobank.account.common.dto.BaseResponse;
import com.demobank.cqrs.core.infra.CommandDispatcher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.MessageFormat;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/openBankAccount")
public class OpenAccountController {

    private final CommandDispatcher commandDispatcher;

    @PostMapping
    public ResponseEntity<BaseResponse> openAccount(@RequestBody OpenAccountCommand command) {
        final var id = UUID.randomUUID().toString();

        command.setId(id);

        try {
            commandDispatcher.send(command);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new OpenAccountResponse(HttpStatus.CREATED.toString(), "bank account creation request completed successfully!", id));
        } catch (IllegalStateException e) {
            log.warn(MessageFormat.format("client made a bad request - {0}.", e));

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new BaseResponse(HttpStatus.BAD_REQUEST.toString(), e.getMessage()));
        } catch (Exception e) {
            final var safeErrMsg = MessageFormat.format("error while processing request to open a new bank account for id - {0}.", id);

            log.error(safeErrMsg, e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new OpenAccountResponse(HttpStatus.INTERNAL_SERVER_ERROR.toString(), safeErrMsg, id));
        }

    }

}
