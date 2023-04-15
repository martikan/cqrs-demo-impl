package com.demobank.account.query.service.consumer;

import com.demobank.account.common.event.AccountClosedEvent;
import com.demobank.account.common.event.AccountOpenedEvent;
import com.demobank.account.common.event.FundsDepositedEvent;
import com.demobank.account.common.event.FundsWithdrawEvent;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;

public interface EventConsumer {
    void consume(@Payload AccountOpenedEvent event, Acknowledgment ack);

    void consume(@Payload AccountClosedEvent event, Acknowledgment ack);

    void consume(@Payload FundsDepositedEvent event, Acknowledgment ack);

    void consume(@Payload FundsWithdrawEvent event, Acknowledgment ack);
}
