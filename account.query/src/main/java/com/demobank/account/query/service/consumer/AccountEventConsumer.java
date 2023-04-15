package com.demobank.account.query.service.consumer;

import com.demobank.account.common.event.AccountClosedEvent;
import com.demobank.account.common.event.AccountOpenedEvent;
import com.demobank.account.common.event.FundsDepositedEvent;
import com.demobank.account.common.event.FundsWithdrawEvent;
import com.demobank.account.query.service.handler.EventHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AccountEventConsumer implements EventConsumer {

    private final EventHandler eventHandler;

    @KafkaListener(topics = "AccountOpenedEvent", groupId = "${groupId}")
    @Override
    public void consume(AccountOpenedEvent event, Acknowledgment ack) {
        eventHandler.on(event);
        ack.acknowledge();
    }

    @KafkaListener(topics = "AccountClosedEvent", groupId = "${groupId}")
    @Override
    public void consume(AccountClosedEvent event, Acknowledgment ack) {
        eventHandler.on(event);
        ack.acknowledge();
    }

    @KafkaListener(topics = "FundsDepositedEvent", groupId = "${groupId}")
    @Override
    public void consume(FundsDepositedEvent event, Acknowledgment ack) {
        eventHandler.on(event);
        ack.acknowledge();
    }

    @KafkaListener(topics = "FundsWithdrawEvent", groupId = "${groupId}")
    @Override
    public void consume(FundsWithdrawEvent event, Acknowledgment ack) {
        eventHandler.on(event);
        ack.acknowledge();
    }
}
