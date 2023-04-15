package com.demobank.account.cmd.api.controller;

import com.demobank.account.cmd.api.command.OpenAccountCommand;
import com.demobank.account.cmd.api.command.RestoreReadDBCommand;
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
@RequestMapping("/v1/restoreReadDB")
public class RestoreReadDBController {

    private final CommandDispatcher commandDispatcher;

    @PostMapping
    public ResponseEntity<BaseResponse> restoreReadDB() {
        try {
            commandDispatcher.send(new RestoreReadDBCommand());

            return ResponseEntity.ok(new BaseResponse(HttpStatus.OK.toString(), "restore read db request completed successfully!"));
        } catch (IllegalStateException e) {
            log.warn(MessageFormat.format("client made a bad request - {0}.", e));

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new BaseResponse(HttpStatus.BAD_REQUEST.toString(), e.getMessage()));
        } catch (Exception e) {
            final var safeErrMsg = "error while processing request to restore read db.";

            log.error(safeErrMsg, e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new BaseResponse(HttpStatus.INTERNAL_SERVER_ERROR.toString(), safeErrMsg));
        }

    }

}
